import express, { type Express, Response, Request } from "express";
import { createServer, type Server } from "http";
import { storage } from "./storage";
import { insertUserSchema, insertEventSchema, insertRegistrationSchema } from "@shared/schema";
import { z } from "zod";
import fs from "fs";
import path from "path";

export async function registerRoutes(app: Express): Promise<Server> {
  // API Routes
  const apiRouter = express.Router();
  app.use("/api", apiRouter);

  // User routes
  apiRouter.get("/users/by-username/:username", async (req: Request, res: Response) => {
    const username = req.params.username;
    
    const user = await storage.getUserByUsername(username);
    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    // Don't send the password
    const { password, ...userWithoutPassword } = user;
    res.json(userWithoutPassword);
  });
  
  apiRouter.get("/users/:id", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.id);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    const user = await storage.getUser(userId);
    if (!user) {
      return res.status(404).json({ message: "User not found" });
    }

    // Don't send the password
    const { password, ...userWithoutPassword } = user;
    res.json(userWithoutPassword);
  });

  apiRouter.post("/users", async (req: Request, res: Response) => {
    try {
      const userData = insertUserSchema.parse(req.body);
      const existingUser = await storage.getUserByUsername(userData.username);
      
      if (existingUser) {
        return res.status(409).json({ message: "Username already exists" });
      }
      
      const newUser = await storage.createUser(userData);
      const { password, ...userWithoutPassword } = newUser;
      res.status(201).json(userWithoutPassword);
    } catch (error) {
      if (error instanceof z.ZodError) {
        return res.status(400).json({ message: "Invalid user data", errors: error.errors });
      }
      res.status(500).json({ message: "Failed to create user" });
    }
  });

  apiRouter.put("/users/:id", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.id);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const userData = req.body;
      const updatedUser = await storage.updateUser(userId, userData);
      
      if (!updatedUser) {
        return res.status(404).json({ message: "User not found" });
      }
      
      const { password, ...userWithoutPassword } = updatedUser;
      res.json(userWithoutPassword);
    } catch (error) {
      res.status(500).json({ message: "Failed to update user" });
    }
  });

  // Event routes
  apiRouter.get("/events", async (_req: Request, res: Response) => {
    const events = await storage.getEvents();
    res.json(events);
  });

  apiRouter.get("/events/featured", async (_req: Request, res: Response) => {
    const events = await storage.getFeaturedEvents();
    res.json(events);
  });

  apiRouter.get("/events/:id", async (req: Request, res: Response) => {
    const eventId = parseInt(req.params.id);
    if (isNaN(eventId)) {
      return res.status(400).json({ message: "Invalid event ID" });
    }

    const event = await storage.getEventWithDetails(eventId);
    if (!event) {
      return res.status(404).json({ message: "Event not found" });
    }

    res.json(event);
  });

  apiRouter.post("/events", async (req: Request, res: Response) => {
    try {
      console.log("Received event data:", req.body);
      
      // Ensure data fields are in the correct format
      const payload = {
        ...req.body,
        // Make sure startDate and endDate are passed as strings that can be parsed to dates
        startDate: req.body.startDate ? new Date(req.body.startDate) : undefined,
        endDate: req.body.endDate ? new Date(req.body.endDate) : undefined,
        // Ensure participantLimit is a number
        participantLimit: req.body.participantLimit ? Number(req.body.participantLimit) : undefined
      };
      
      const eventData = insertEventSchema.parse(payload);
      console.log("Parsed event data:", eventData);
      
      const newEvent = await storage.createEvent(eventData);
      res.status(201).json(newEvent);
    } catch (error) {
      console.error("Event creation error:", error);
      if (error instanceof z.ZodError) {
        return res.status(400).json({ message: "Invalid event data", errors: error.errors });
      }
      res.status(500).json({ message: "Failed to create event" });
    }
  });

  apiRouter.put("/events/:id", async (req: Request, res: Response) => {
    const eventId = parseInt(req.params.id);
    if (isNaN(eventId)) {
      return res.status(400).json({ message: "Invalid event ID" });
    }

    try {
      const eventData = req.body;
      const updatedEvent = await storage.updateEvent(eventId, eventData);
      
      if (!updatedEvent) {
        return res.status(404).json({ message: "Event not found" });
      }
      
      res.json(updatedEvent);
    } catch (error) {
      res.status(500).json({ message: "Failed to update event" });
    }
  });

  apiRouter.delete("/events/:id", async (req: Request, res: Response) => {
    const eventId = parseInt(req.params.id);
    if (isNaN(eventId)) {
      return res.status(400).json({ message: "Invalid event ID" });
    }

    const success = await storage.deleteEvent(eventId);
    if (!success) {
      return res.status(404).json({ message: "Event not found" });
    }

    res.status(204).end();
  });

  // Registration routes
  apiRouter.post("/registrations", async (req: Request, res: Response) => {
    try {
      const registrationData = insertRegistrationSchema.parse(req.body);
      
      // Check if user is already registered for this event
      const existingRegistration = await storage.getRegistration(
        registrationData.userId, 
        registrationData.eventId
      );
      
      if (existingRegistration) {
        return res.status(409).json({ message: "User is already registered for this event" });
      }
      
      const newRegistration = await storage.registerForEvent(registrationData);
      res.status(201).json(newRegistration);
    } catch (error) {
      if (error instanceof z.ZodError) {
        return res.status(400).json({ message: "Invalid registration data", errors: error.errors });
      }
      res.status(500).json({ message: "Failed to register for event" });
    }
  });

  apiRouter.put("/registrations/:id/status", async (req: Request, res: Response) => {
    const registrationId = parseInt(req.params.id);
    if (isNaN(registrationId)) {
      return res.status(400).json({ message: "Invalid registration ID" });
    }

    const { status } = req.body;
    if (!status || typeof status !== "string") {
      return res.status(400).json({ message: "Status is required" });
    }

    try {
      const updatedRegistration = await storage.updateRegistrationStatus(registrationId, status);
      
      if (!updatedRegistration) {
        return res.status(404).json({ message: "Registration not found" });
      }
      
      res.json(updatedRegistration);
    } catch (error) {
      res.status(500).json({ message: "Failed to update registration status" });
    }
  });

  // Dashboard routes
  apiRouter.get("/dashboard/registered/:userId", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.userId);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const events = await storage.getUserRegisteredEvents(userId);
      res.json(events);
    } catch (error) {
      res.status(500).json({ message: "Failed to fetch registered events" });
    }
  });

  apiRouter.get("/dashboard/organized/:userId", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.userId);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const events = await storage.getUserOrganizedEvents(userId);
      res.json(events);
    } catch (error) {
      res.status(500).json({ message: "Failed to fetch organized events" });
    }
  });

  apiRouter.get("/dashboard/stats/:userId", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.userId);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const stats = await storage.getUserEventStats(userId);
      res.json(stats);
    } catch (error) {
      res.status(500).json({ message: "Failed to fetch user stats" });
    }
  });

  // Certificate and Award routes
  apiRouter.get("/users/:userId/certificates", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.userId);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const certificates = await storage.getUserCertificates(userId);
      res.json(certificates);
    } catch (error) {
      res.status(500).json({ message: "Failed to fetch certificates" });
    }
  });

  apiRouter.get("/users/:userId/awards", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.userId);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const awards = await storage.getUserAwards(userId);
      res.json(awards);
    } catch (error) {
      res.status(500).json({ message: "Failed to fetch awards" });
    }
  });

  // AI Report generation
  apiRouter.get("/users/:userId/ai-report", async (req: Request, res: Response) => {
    const userId = parseInt(req.params.userId);
    if (isNaN(userId)) {
      return res.status(400).json({ message: "Invalid user ID" });
    }

    try {
      const reportContent = await storage.generateAIReport(userId);
      
      // Return the report as plain text
      res.setHeader('Content-Type', 'text/plain');
      res.setHeader('Content-Disposition', 'attachment; filename="ai-event-report.txt"');
      res.send(reportContent);
    } catch (error) {
      res.status(500).json({ message: "Failed to generate AI report" });
    }
  });

  const httpServer = createServer(app);
  return httpServer;
}
