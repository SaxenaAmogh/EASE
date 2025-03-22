import { useQuery } from '@tanstack/react-query';
import { Link } from 'wouter';
import { formatEventDate } from '@/lib/utils';
import { Event } from '@shared/schema';

export default function FeaturedEvents() {
  const { data: events = [], isLoading, error } = useQuery<Event[]>({
    queryKey: ['/api/events/featured'],
  });

  return (
    <section id="events" className="py-12 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center">
          <h2 className="text-3xl font-extrabold text-gray-900 sm:text-4xl font-poppins">
            Featured Events
          </h2>
          <p className="mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4">
            Discover exciting hackathons and tech events happening soon.
          </p>
        </div>

        {isLoading ? (
          <div className="mt-12 flex justify-center">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
          </div>
        ) : error ? (
          <div className="mt-12 text-center text-red-500">
            Failed to load events. Please try again later.
          </div>
        ) : (
          <>
            <div className="mt-12 grid gap-8 md:grid-cols-2 lg:grid-cols-3">
              {events.map((event) => (
                <div key={event.id} className="flex flex-col rounded-lg shadow-lg overflow-hidden transition-all duration-300 hover:shadow-xl transform hover:-translate-y-1">
                  <div className="flex-shrink-0">
                    <img 
                      className="h-48 w-full object-cover" 
                      src={event.image || "https://images.unsplash.com/photo-1505373877841-8d25f7d46678"} 
                      alt={event.title} 
                    />
                  </div>
                  <div className="flex-1 bg-white p-6 flex flex-col justify-between">
                    <div className="flex-1">
                      <p className="text-sm font-medium text-primary">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          event.eventType === 'Hackathon' ? 'bg-blue-100 text-blue-800' :
                          event.eventType === 'Conference' ? 'bg-green-100 text-green-800' :
                          'bg-purple-100 text-purple-800'
                        }`}>
                          {event.eventType}
                        </span>
                      </p>
                      <Link 
                        href={`/event/${event.id}`}
                        className="block mt-2"
                      >
                        <p className="text-xl font-semibold text-gray-900 font-poppins">{event.title}</p>
                        <div className="flex items-center mt-2 text-sm text-gray-500">
                          <i className="far fa-calendar-alt mr-2 text-primary"></i>
                          <span>{formatEventDate(event.startDate, event.endDate)}</span>
                        </div>
                        <div className="flex items-center mt-1 text-sm text-gray-500">
                          <i className="fas fa-map-marker-alt mr-2 text-primary"></i>
                          <span>{event.location}</span>
                        </div>
                        <p className="mt-3 text-base text-gray-500">
                          {event.description.length > 150 
                            ? `${event.description.substring(0, 150)}...` 
                            : event.description}
                        </p>
                      </Link>
                    </div>
                    <div className="mt-6 flex items-center">
                      <div className="flex-shrink-0">
                        <img 
                          className="h-10 w-10 rounded-full" 
                          src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&auto=format&fit=crop&w=40&h=40&q=80" 
                          alt="Organizer" 
                        />
                      </div>
                      <div className="ml-3">
                        <p className="text-sm font-medium text-gray-900">
                          {event.organizerId === 1 ? 'Alex Johnson' : 'TechCorp'}
                        </p>
                        <div className="flex space-x-1 text-sm text-gray-500">
                          <span>{event.participantLimit} participants</span>
                          {event.prizePool && (
                            <>
                              <span aria-hidden="true">&middot;</span>
                              <span>{event.prizePool} prize pool</span>
                            </>
                          )}
                        </div>
                      </div>
                    </div>
                    <div className="mt-4">
                      <Link 
                        href={`/event/${event.id}`}
                        className="text-primary font-medium hover:text-blue-600 transition-colors duration-200"
                      >
                        View Details <i className="fas fa-arrow-right ml-1"></i>
                      </Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <div className="mt-10 text-center">
              <Link 
                href="/events"
                className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-primary hover:bg-blue-600 transition-colors duration-200"
              >
                View All Events
                <i className="fas fa-chevron-right ml-2"></i>
              </Link>
            </div>
          </>
        )}
      </div>
    </section>
  );
}
