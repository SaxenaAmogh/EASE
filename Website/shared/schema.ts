import { pgTable, text, serial, integer, timestamp, boolean } from "drizzle-orm/pg-core";
import { createInsertSchema } from "drizzle-zod";
import { z } from "zod";

export const users = pgTable("users", {
  id: serial("id").primaryKey(),
  username: text("username").notNull().unique(),
  password: text("password").notNull(),
  fullname: text("fullname").notNull(),
  email: text("email").notNull().unique(),
  phone: text("phone"),
  location: text("location"),
  about: text("about"),
  profileImage: text("profile_image"),
  memberSince: timestamp("member_since").defaultNow().notNull(),
});

export const events = pgTable("events", {
  id: serial("id").primaryKey(),
  title: text("title").notNull(),
  description: text("description").notNull(),
  startDate: timestamp("start_date").notNull(),
  endDate: timestamp("end_date").notNull(),
  location: text("location").notNull(),
  image: text("image"),
  eventType: text("event_type").notNull(), // Hackathon, Conference, etc.
  organizerId: integer("organizer_id").notNull(),
  participantLimit: integer("participant_limit"),
  prizePool: text("prize_pool"),
  createdAt: timestamp("created_at").defaultNow().notNull(),
});

export const registrations = pgTable("registrations", {
  id: serial("id").primaryKey(),
  userId: integer("user_id").notNull(),
  eventId: integer("event_id").notNull(),
  status: text("status").notNull(), // Confirmed, Pending
  registeredAt: timestamp("registered_at").defaultNow().notNull(),
});

export const certificates = pgTable("certificates", {
  id: serial("id").primaryKey(),
  userId: integer("user_id").notNull(),
  eventId: integer("event_id").notNull(),
  name: text("name").notNull(),
  awardedAt: timestamp("awarded_at").defaultNow().notNull(),
});

export const awards = pgTable("awards", {
  id: serial("id").primaryKey(),
  userId: integer("user_id").notNull(),
  eventId: integer("event_id").notNull(),
  name: text("name").notNull(),
  awardedAt: timestamp("awarded_at").defaultNow().notNull(),
});

// Insert Schemas
export const insertUserSchema = createInsertSchema(users).omit({ 
  id: true, 
  memberSince: true 
});

export const insertEventSchema = createInsertSchema(events).omit({ 
  id: true, 
  createdAt: true 
});

export const insertRegistrationSchema = createInsertSchema(registrations).omit({ 
  id: true, 
  registeredAt: true 
});

export const insertCertificateSchema = createInsertSchema(certificates).omit({ 
  id: true, 
  awardedAt: true 
});

export const insertAwardSchema = createInsertSchema(awards).omit({ 
  id: true, 
  awardedAt: true 
});

// Types
export type User = typeof users.$inferSelect;
export type InsertUser = z.infer<typeof insertUserSchema>;

export type Event = typeof events.$inferSelect;
export type InsertEvent = z.infer<typeof insertEventSchema>;

export type Registration = typeof registrations.$inferSelect;
export type InsertRegistration = z.infer<typeof insertRegistrationSchema>;

export type Certificate = typeof certificates.$inferSelect;
export type InsertCertificate = z.infer<typeof insertCertificateSchema>;

export type Award = typeof awards.$inferSelect;
export type InsertAward = z.infer<typeof insertAwardSchema>;

// Extended Schema for Dashboard Data
export type EventWithDetails = Event & {
  organizer?: User;
  participantCount?: number;
}

export type UserEventStats = {
  eventsAttended: number;
  eventsOrganized: number;
  certificatesEarned: number;
  awardsWon: number;
}
