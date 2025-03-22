import { useState, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'wouter';
import Navbar from '@/components/Navbar';
import { Event } from '@shared/schema';
import { formatEventDate } from '@/lib/utils';

export default function EventsPage() {
  const [filter, setFilter] = useState('all');
  
  const { data: events = [], isLoading } = useQuery<Event[]>({
    queryKey: ['/api/events'],
    refetchOnMount: true
  });

  // Group events by type when showing all events
  const eventsByType = useMemo(() => {
    if (filter !== 'all') {
      return {
        [filter.charAt(0).toUpperCase() + filter.slice(1)]: events.filter(
          event => event.eventType.toLowerCase() === filter
        )
      };
    }

    const groups = events.reduce((acc, event) => {
      const type = event.eventType;
      if (!acc[type]) {
        acc[type] = [];
      }
      acc[type].push(event);
      return acc;
    }, {} as Record<string, Event[]>);

    // Ensure special categories exist even if empty
    ['Hackathon', 'Conference', 'Workshop', 'Meetup', 'Fests', 'Others'].forEach(type => {
      if (!groups[type]) {
        groups[type] = [];
      }
    });

    return groups;
  }, [events, filter]);

  // Get event types in a specific order (with Hackathon first)
  const eventTypes = useMemo(() => {
    const types = Object.keys(eventsByType);
    // Sort to ensure Hackathon is first, then other types alphabetically
    return types.sort((a, b) => {
      if (a === 'Hackathon') return -1;
      if (b === 'Hackathon') return 1;
      return a.localeCompare(b);
    });
  }, [eventsByType]);

  // Render an event card
  const renderEventCard = (event: Event) => (
    <div key={event.id} className="flex flex-col rounded-lg shadow-lg overflow-hidden transition-all duration-300 hover:shadow-xl transform hover:-translate-y-1">
      <div className="flex-shrink-0">
        <img 
          className="h-48 w-full object-cover" 
          src={event.image || 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&h=400&q=80'} 
          alt={event.title} 
        />
      </div>
      <div className="flex-1 bg-white p-6 flex flex-col justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-primary">
            <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
              event.eventType === 'Hackathon' ? 'bg-blue-100 text-blue-800' :
              event.eventType === 'Conference' ? 'bg-green-100 text-green-800' :
              event.eventType === 'Workshop' ? 'bg-purple-100 text-purple-800' :
              event.eventType === 'Meetup' ? 'bg-red-100 text-red-800' :
              event.eventType === 'Fests' ? 'bg-yellow-100 text-yellow-800' :
              'bg-gray-100 text-gray-800'
            }`}>
              {event.eventType}
            </span>
          </p>
          <Link href={`/event/${event.id}`}>
            <div className="block mt-2 cursor-pointer">
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
            </div>
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
          <Link href={`/event/${event.id}`}>
            <span className="text-primary font-medium hover:text-blue-600 transition-colors duration-200 cursor-pointer">
              View Details <i className="fas fa-arrow-right ml-1"></i>
            </span>
          </Link>
        </div>
      </div>
    </div>
  );

  return (
    <div className="font-sans bg-gray-50 min-h-screen">
      <Navbar />
      <div className="pt-16">
        <div className="bg-blue-500 py-16">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h1 className="text-4xl font-extrabold text-white sm:text-5xl font-poppins">
              Discover Events
            </h1>
            <p className="mt-3 max-w-md mx-auto text-xl text-blue-100 sm:text-2xl">
              Find and join exciting hackathons, conferences, and workshops.
            </p>
          </div>
        </div>

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="flex flex-wrap items-center justify-between mb-8">
            <h2 className="text-2xl font-bold text-gray-900 mb-4 md:mb-0">
              {filter === 'all' ? 'All Events' : `${filter.charAt(0).toUpperCase() + filter.slice(1)}s`}
            </h2>
            <div className="flex flex-wrap gap-2">
              <button
                onClick={() => setFilter('all')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'all'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                All
              </button>
              <button
                onClick={() => setFilter('hackathon')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'hackathon'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                Hackathons
              </button>
              <button
                onClick={() => setFilter('conference')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'conference'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                Conferences
              </button>
              <button
                onClick={() => setFilter('workshop')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'workshop'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                Workshops
              </button>
              <button
                onClick={() => setFilter('meetup')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'meetup'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                Meetups
              </button>
              <button
                onClick={() => setFilter('fests')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'fests'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                Fests
              </button>
              <button
                onClick={() => setFilter('others')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  filter === 'others'
                    ? 'bg-primary text-white'
                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                }`}
              >
                Others
              </button>
            </div>
          </div>

          {isLoading ? (
            <div className="flex justify-center my-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
            </div>
          ) : Object.keys(eventsByType).length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">No events found matching your filter criteria.</p>
            </div>
          ) : (
            <div className="space-y-12">
              {eventTypes.map(eventType => (
                <div key={eventType} className="mb-8">
                  {filter === 'all' && (
                    <h3 className="text-xl font-bold text-gray-800 mb-6 pb-2 border-b border-gray-200">
                      {eventType}s
                    </h3>
                  )}
                  <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3">
                    {eventsByType[eventType].map(event => renderEventCard(event))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
