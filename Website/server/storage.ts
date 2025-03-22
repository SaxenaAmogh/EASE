import { 
  users, User, InsertUser, 
  events, Event, InsertEvent, 
  registrations, Registration, InsertRegistration,
  certificates, Certificate, InsertCertificate,
  awards, Award, InsertAward,
  EventWithDetails, UserEventStats
} from "@shared/schema";

export interface IStorage {
  // User operations
  getUser(id: number): Promise<User | undefined>;
  getUserByUsername(username: string): Promise<User | undefined>;
  createUser(user: InsertUser): Promise<User>;
  updateUser(id: number, user: Partial<InsertUser>): Promise<User | undefined>;

  // Event operations
  getEvent(id: number): Promise<Event | undefined>;
  getEventWithDetails(id: number): Promise<EventWithDetails | undefined>;
  getEvents(): Promise<Event[]>;
  getFeaturedEvents(limit?: number): Promise<Event[]>;
  createEvent(event: InsertEvent): Promise<Event>;
  updateEvent(id: number, event: Partial<InsertEvent>): Promise<Event | undefined>;
  deleteEvent(id: number): Promise<boolean>;

  // Registration operations
  registerForEvent(registration: InsertRegistration): Promise<Registration>;
  getRegistration(userId: number, eventId: number): Promise<Registration | undefined>;
  getRegistrationsByUser(userId: number): Promise<Registration[]>;
  getRegistrationsByEvent(eventId: number): Promise<Registration[]>;
  updateRegistrationStatus(id: number, status: string): Promise<Registration | undefined>;

  // Dashboard operations
  getUserRegisteredEvents(userId: number): Promise<EventWithDetails[]>;
  getUserOrganizedEvents(userId: number): Promise<EventWithDetails[]>;
  getUserEventStats(userId: number): Promise<UserEventStats>;

  // Certificate and Award operations
  getUserCertificates(userId: number): Promise<Certificate[]>;
  getUserAwards(userId: number): Promise<Award[]>;
  createCertificate(certificate: InsertCertificate): Promise<Certificate>;
  createAward(award: InsertAward): Promise<Award>;

  // AI Report - simulated for now
  generateAIReport(userId: number): Promise<string>;
}

export class MemStorage implements IStorage {
  private users: Map<number, User>;
  private events: Map<number, Event>;
  private registrations: Map<number, Registration>;
  private certificates: Map<number, Certificate>;
  private awards: Map<number, Award>;
  
  private userId: number;
  private eventId: number;
  private registrationId: number;
  private certificateId: number;
  private awardId: number;

  constructor() {
    this.users = new Map();
    this.events = new Map();
    this.registrations = new Map();
    this.certificates = new Map();
    this.awards = new Map();
    
    this.userId = 1;
    this.eventId = 1;
    this.registrationId = 1;
    this.certificateId = 1;
    this.awardId = 1;
    
    // Add initial data
    this.initializeData();
  }

  private initializeData() {
    // Create sample users
    const sampleUsers = [
      {
        username: "alexjohnson",
        password: "password123",
        fullname: "Alex Johnson",
        email: "alex.johnson@example.com",
        phone: "(555) 123-4567",
        location: "San Francisco, California",
        about: "Frontend developer passionate about creating intuitive user experiences. Interested in hackathons and tech conferences.",
        profileImage: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e",
        memberSince: new Date("2023-01-01")
      },
      {
        username: "techcorp",
        password: "password123",
        fullname: "TechCorp",
        email: "info@techcorp.com",
        phone: "(555) 987-6543",
        location: "San Francisco, California",
        about: "Leading tech company organizing innovative events and hackathons.",
        profileImage: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e",
        memberSince: new Date("2022-05-15")
      }
    ];

    sampleUsers.forEach(user => this.createUser(user));

    // Create sample events
    const sampleEvents = [
      {
        title: "Tech Innovate Hackathon",
        description: "A 48-hour coding challenge to build innovative solutions for real-world problems. Cash prizes and networking opportunities.",
        startDate: new Date("2023-06-15"),
        endDate: new Date("2023-06-17"),
        location: "San Francisco, CA",
        image: "https://images.unsplash.com/photo-1540575467063-178a50c2df87",
        eventType: "Hackathon",
        organizerId: 2,
        participantLimit: 300,
        prizePool: "$5,000"
      },
      {
        title: "AI Summit 2023",
        description: "Explore the latest advancements in artificial intelligence with industry leaders. Workshops, keynotes, and networking.",
        startDate: new Date("2023-07-10"),
        endDate: new Date("2023-07-12"),
        location: "New York, NY",
        image: "https://images.unsplash.com/photo-1517048676732-d65bc937f952",
        eventType: "Conference",
        organizerId: 2,
        participantLimit: 1200,
        prizePool: null
      },
      {
        title: "Code for Good",
        description: "Build technology solutions for nonprofit organizations. Make a positive impact while showcasing your programming skills.",
        startDate: new Date("2023-08-05"),
        endDate: new Date("2023-08-07"),
        location: "Austin, TX",
        image: "https://images.unsplash.com/photo-1505373877841-8d25f7d46678",
        eventType: "Hackathon",
        organizerId: 2,
        participantLimit: 250,
        prizePool: null
      },
      {
        title: "Web Development Workshop",
        description: "Learn modern web development techniques from industry experts.",
        startDate: new Date("2023-05-05"),
        endDate: new Date("2023-05-05"),
        location: "Online",
        image: "https://images.unsplash.com/photo-1523580494863-6f3031224c94",
        eventType: "Workshop",
        organizerId: 1,
        participantLimit: 100,
        prizePool: null
      },
      {
        title: "Product Design Meetup",
        description: "Connect with product designers and learn about the latest design trends.",
        startDate: new Date("2023-09-12"),
        endDate: new Date("2023-09-12"),
        location: "Chicago, IL",
        image: "https://images.unsplash.com/photo-1543269865-cbf427effbad",
        eventType: "Meetup",
        organizerId: 1,
        participantLimit: 50,
        prizePool: null
      }
    ];

    sampleEvents.forEach(event => this.createEvent(event));

    // Create sample registrations
    this.registerForEvent({
      userId: 1,
      eventId: 1,
      status: "Confirmed"
    });

    this.registerForEvent({
      userId: 1,
      eventId: 2,
      status: "Pending"
    });

    // Add certificates and awards
    for (let i = 0; i < 8; i++) {
      this.createCertificate({
        userId: 1,
        eventId: i % 3 + 1,
        name: `Certificate for Event ${i % 3 + 1}`
      });
    }

    for (let i = 0; i < 3; i++) {
      this.createAward({
        userId: 1,
        eventId: i % 3 + 1,
        name: `Award for Event ${i % 3 + 1}`
      });
    }
  }

  // User operations
  async getUser(id: number): Promise<User | undefined> {
    return this.users.get(id);
  }

  async getUserByUsername(username: string): Promise<User | undefined> {
    return Array.from(this.users.values()).find(
      (user) => user.username === username
    );
  }

  async createUser(user: InsertUser): Promise<User> {
    const id = this.userId++;
    const newUser: User = { 
      ...user, 
      id, 
      memberSince: user.memberSince || new Date() 
    };
    this.users.set(id, newUser);
    return newUser;
  }

  async updateUser(id: number, userData: Partial<InsertUser>): Promise<User | undefined> {
    const user = await this.getUser(id);
    if (!user) return undefined;

    const updatedUser = { ...user, ...userData };
    this.users.set(id, updatedUser);
    return updatedUser;
  }

  // Event operations
  async getEvent(id: number): Promise<Event | undefined> {
    return this.events.get(id);
  }

  async getEventWithDetails(id: number): Promise<EventWithDetails | undefined> {
    const event = await this.getEvent(id);
    if (!event) return undefined;

    const organizer = await this.getUser(event.organizerId);
    const registrations = await this.getRegistrationsByEvent(id);

    return {
      ...event,
      organizer,
      participantCount: registrations.length
    };
  }

  async getEvents(): Promise<Event[]> {
    return Array.from(this.events.values());
  }

  async getFeaturedEvents(limit: number = 6): Promise<Event[]> {
    // Sort by date and return the most recent events
    return Array.from(this.events.values())
      .sort((a, b) => new Date(b.startDate).getTime() - new Date(a.startDate).getTime())
      .slice(0, limit);
  }

  async createEvent(event: InsertEvent): Promise<Event> {
    const id = this.eventId++;
    const newEvent: Event = { 
      ...event, 
      id, 
      createdAt: new Date() 
    };
    this.events.set(id, newEvent);
    return newEvent;
  }

  async updateEvent(id: number, eventData: Partial<InsertEvent>): Promise<Event | undefined> {
    const event = await this.getEvent(id);
    if (!event) return undefined;

    const updatedEvent = { ...event, ...eventData };
    this.events.set(id, updatedEvent);
    return updatedEvent;
  }

  async deleteEvent(id: number): Promise<boolean> {
    return this.events.delete(id);
  }

  // Registration operations
  async registerForEvent(registration: InsertRegistration): Promise<Registration> {
    const id = this.registrationId++;
    const newRegistration: Registration = { 
      ...registration, 
      id, 
      registeredAt: new Date() 
    };
    this.registrations.set(id, newRegistration);
    return newRegistration;
  }

  async getRegistration(userId: number, eventId: number): Promise<Registration | undefined> {
    return Array.from(this.registrations.values()).find(
      (registration) => registration.userId === userId && registration.eventId === eventId
    );
  }

  async getRegistrationsByUser(userId: number): Promise<Registration[]> {
    return Array.from(this.registrations.values()).filter(
      (registration) => registration.userId === userId
    );
  }

  async getRegistrationsByEvent(eventId: number): Promise<Registration[]> {
    return Array.from(this.registrations.values()).filter(
      (registration) => registration.eventId === eventId
    );
  }

  async updateRegistrationStatus(id: number, status: string): Promise<Registration | undefined> {
    const registration = this.registrations.get(id);
    if (!registration) return undefined;

    const updatedRegistration = { ...registration, status };
    this.registrations.set(id, updatedRegistration);
    return updatedRegistration;
  }

  // Dashboard operations
  async getUserRegisteredEvents(userId: number): Promise<EventWithDetails[]> {
    const registrations = await this.getRegistrationsByUser(userId);
    const events: EventWithDetails[] = [];

    for (const registration of registrations) {
      const event = await this.getEventWithDetails(registration.eventId);
      if (event) {
        events.push({
          ...event,
          // Add registration status to the event
          status: registration.status
        } as EventWithDetails);
      }
    }

    return events;
  }

  async getUserOrganizedEvents(userId: number): Promise<EventWithDetails[]> {
    const allEvents = await this.getEvents();
    const organizedEvents = allEvents.filter(event => event.organizerId === userId);
    
    const eventsWithDetails: EventWithDetails[] = [];
    
    for (const event of organizedEvents) {
      const registrations = await this.getRegistrationsByEvent(event.id);
      eventsWithDetails.push({
        ...event,
        participantCount: registrations.length
      });
    }
    
    return eventsWithDetails;
  }

  async getUserEventStats(userId: number): Promise<UserEventStats> {
    const registeredEvents = await this.getUserRegisteredEvents(userId);
    const organizedEvents = await this.getUserOrganizedEvents(userId);
    const userCertificates = await this.getUserCertificates(userId);
    const userAwards = await this.getUserAwards(userId);

    return {
      eventsAttended: registeredEvents.length,
      eventsOrganized: organizedEvents.length,
      certificatesEarned: userCertificates.length,
      awardsWon: userAwards.length
    };
  }

  // Certificate and Award operations
  async getUserCertificates(userId: number): Promise<Certificate[]> {
    return Array.from(this.certificates.values()).filter(
      (certificate) => certificate.userId === userId
    );
  }

  async getUserAwards(userId: number): Promise<Award[]> {
    return Array.from(this.awards.values()).filter(
      (award) => award.userId === userId
    );
  }

  async createCertificate(certificate: InsertCertificate): Promise<Certificate> {
    const id = this.certificateId++;
    const newCertificate: Certificate = { 
      ...certificate, 
      id, 
      awardedAt: new Date() 
    };
    this.certificates.set(id, newCertificate);
    return newCertificate;
  }

  async createAward(award: InsertAward): Promise<Award> {
    const id = this.awardId++;
    const newAward: Award = { 
      ...award, 
      id, 
      awardedAt: new Date() 
    };
    this.awards.set(id, newAward);
    return newAward;
  }

  // AI Report - simulated for now
  async generateAIReport(userId: number): Promise<string> {
    const stats = await this.getUserEventStats(userId);
    const user = await this.getUser(userId);
    
    if (!user) {
      throw new Error("User not found");
    }
    
    // Simulate a report - in a real app, this would be generated by an AI service
    const reportContent = `
AI-Generated Event Participation Report for ${user.fullname}

Summary of Activity:
- Total Events Attended: ${stats.eventsAttended}
- Events Organized: ${stats.eventsOrganized}
- Certificates Earned: ${stats.certificatesEarned}
- Awards Won: ${stats.awardsWon}

Analysis:
Based on your participation, you show a strong interest in technical events, particularly in hackathons. 
Your active involvement in both attending and organizing events demonstrates leadership and community engagement.

Recommendations:
1. Consider participating in more specialized conferences in your field
2. Your organization skills could be leveraged for larger events
3. Share your expertise by mentoring at upcoming hackathons
4. Explore opportunities for speaking engagements at industry events

Generated on: ${new Date().toLocaleDateString()}
`;

    return reportContent;
  }
}

export const storage = new MemStorage();
