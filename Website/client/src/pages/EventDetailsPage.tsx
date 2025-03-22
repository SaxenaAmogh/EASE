import { useQuery } from '@tanstack/react-query';
import { useParams, Link } from 'wouter';
import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';
import { EventWithDetails } from '@shared/schema';
import { formatEventDate } from '@/lib/utils';
import { useToast } from '@/hooks/use-toast';
import { apiRequest } from '@/lib/queryClient';

export default function EventDetailsPage() {
  const { id } = useParams();
  const eventId = parseInt(id);
  const { toast } = useToast();
  
  const { data: event, isLoading, error } = useQuery<EventWithDetails>({
    queryKey: [`/api/events/${eventId}`],
  });

  const handleRegister = async () => {
    try {
      await apiRequest('POST', '/api/registrations', {
        userId: 1, // Hardcoded for demo
        eventId: eventId,
        status: 'Pending'
      });
      
      toast({
        title: "Registration Successful",
        description: "You have been registered for this event!",
      });
    } catch (error) {
      toast({
        title: "Registration Failed",
        description: "You may already be registered for this event or an error occurred.",
        variant: "destructive",
      });
    }
  };

  if (isLoading) {
    return (
      <div className="font-sans bg-gray-50 min-h-screen">
        <Navbar />
        <div className="pt-16 flex justify-center items-center min-h-[500px]">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
        </div>
      </div>
    );
  }

  if (error || !event) {
    return (
      <div className="font-sans bg-gray-50 min-h-screen">
        <Navbar />
        <div className="pt-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="text-center">
            <h1 className="text-3xl font-extrabold text-gray-900">Event Not Found</h1>
            <p className="mt-4 text-gray-500">The event you're looking for doesn't exist or there was an error loading it.</p>
            <Link href="/events">
              <a className="mt-6 inline-block text-primary hover:text-blue-600">Back to Events</a>
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="font-sans bg-gray-50 min-h-screen">
      <Navbar />
      <div className="pt-16">
        <div className="relative h-96">
          <img 
            src={event.image} 
            alt={event.title} 
            className="w-full h-full object-cover" 
          />
          <div className="absolute inset-0 bg-gradient-to-b from-transparent to-black opacity-70"></div>
          <div className="absolute bottom-0 left-0 right-0 p-8">
            <div className="max-w-7xl mx-auto">
              <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                event.eventType === 'Hackathon' ? 'bg-blue-100 text-blue-800' :
                event.eventType === 'Conference' ? 'bg-green-100 text-green-800' :
                'bg-purple-100 text-purple-800'
              }`}>
                {event.eventType}
              </span>
              <h1 className="mt-2 text-4xl font-extrabold text-white font-poppins">{event.title}</h1>
              <div className="mt-4 flex flex-wrap gap-4 text-white">
                <div className="flex items-center">
                  <i className="far fa-calendar-alt mr-2"></i>
                  <span>{formatEventDate(event.startDate, event.endDate)}</span>
                </div>
                <div className="flex items-center">
                  <i className="fas fa-map-marker-alt mr-2"></i>
                  <span>{event.location}</span>
                </div>
                <div className="flex items-center">
                  <i className="fas fa-user-friends mr-2"></i>
                  <span>{event.participantCount || 0} / {event.participantLimit || "∞"} participants</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2">
              <div className="bg-white shadow rounded-lg p-6">
                <h2 className="text-2xl font-bold text-gray-900 font-poppins mb-4">About This Event</h2>
                <p className="text-gray-700 whitespace-pre-line">{event.description}</p>
                
                <div className="mt-8">
                  <h3 className="text-xl font-semibold text-gray-900 font-poppins mb-3">What to Expect</h3>
                  <ul className="space-y-2 text-gray-700">
                    <li className="flex items-start">
                      <i className="fas fa-check-circle text-green-500 mt-1 mr-2"></i>
                      <span>Networking opportunities with industry professionals</span>
                    </li>
                    <li className="flex items-start">
                      <i className="fas fa-check-circle text-green-500 mt-1 mr-2"></i>
                      <span>Hands-on workshops and learning sessions</span>
                    </li>
                    <li className="flex items-start">
                      <i className="fas fa-check-circle text-green-500 mt-1 mr-2"></i>
                      <span>Opportunity to showcase your skills and creativity</span>
                    </li>
                    {event.prizePool && (
                      <li className="flex items-start">
                        <i className="fas fa-check-circle text-green-500 mt-1 mr-2"></i>
                        <span>Compete for prizes worth {event.prizePool}</span>
                      </li>
                    )}
                  </ul>
                </div>
              </div>
            </div>
            
            <div>
              <div className="bg-white shadow rounded-lg p-6 sticky top-20">
                <h2 className="text-xl font-bold text-gray-900 font-poppins mb-4">Registration</h2>
                
                <div className="mb-4">
                  <div className="flex justify-between mb-2">
                    <span className="text-gray-600">Registration Status</span>
                    <span className="text-primary font-medium">Open</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-primary h-2 rounded-full" 
                      style={{ width: event.participantCount && event.participantLimit 
                        ? `${Math.min((event.participantCount / event.participantLimit) * 100, 100)}%` 
                        : '10%' 
                      }}
                    ></div>
                  </div>
                  <p className="text-sm text-gray-500 mt-1">
                    {event.participantCount || 0} out of {event.participantLimit || "unlimited"} spots filled
                  </p>
                </div>
                
                <button
                  onClick={handleRegister}
                  className="w-full bg-primary hover:bg-blue-600 text-white font-medium py-3 px-4 rounded-md transition-colors duration-300"
                >
                  Register Now
                </button>
                
                <div className="mt-6">
                  <h3 className="text-lg font-semibold text-gray-900 font-poppins mb-3">Organizer</h3>
                  <div className="flex items-center">
                    <img 
                      className="h-12 w-12 rounded-full" 
                      src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&auto=format&fit=crop&w=48&h=48&q=80" 
                      alt="Organizer" 
                    />
                    <div className="ml-3">
                      <p className="text-base font-medium text-gray-900">
                        {event.organizerId === 1 ? 'Alex Johnson' : 'TechCorp'}
                      </p>
                      <p className="text-sm text-gray-500">Event Organizer</p>
                    </div>
                  </div>
                </div>
                
                <div className="mt-6">
                  <h3 className="text-lg font-semibold text-gray-900 font-poppins mb-3">Share This Event</h3>
                  <div className="flex space-x-4">
                    <a href="#" className="text-gray-400 hover:text-blue-500">
                      <i className="fab fa-facebook-f"></i>
                    </a>
                    <a href="#" className="text-gray-400 hover:text-blue-400">
                      <i className="fab fa-twitter"></i>
                    </a>
                    <a href="#" className="text-gray-400 hover:text-blue-600">
                      <i className="fab fa-linkedin-in"></i>
                    </a>
                    <a href="#" className="text-gray-400 hover:text-green-500">
                      <i className="fas fa-envelope"></i>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <Footer />
      </div>
    </div>
  );
}
