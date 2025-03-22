import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Link } from 'wouter';
import { EventWithDetails } from '@shared/schema';
import { formatEventDate } from '@/lib/utils';

export default function Dashboard() {
  const [activeTab, setActiveTab] = useState('registered');
  
  const { data: registeredEvents = [], isLoading: loadingRegistered } = useQuery<EventWithDetails[]>({
    queryKey: ['/api/dashboard/registered/1'],
  });
  
  const { data: organizedEvents = [], isLoading: loadingOrganized } = useQuery<EventWithDetails[]>({
    queryKey: ['/api/dashboard/organized/1'],
  });

  return (
    <section id="dashboard" className="py-12 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center">
          <h2 className="text-3xl font-extrabold text-gray-900 sm:text-4xl font-poppins">
            Your Event Dashboard
          </h2>
          <p className="mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4">
            Track your registered events and organized events all in one place.
          </p>
        </div>

        <div className="mt-12">
          <div className="bg-white shadow rounded-lg">
            <div className="border-b border-gray-200">
              <nav className="flex -mb-px">
                <a 
                  href="#registered" 
                  className={`${activeTab === 'registered' ? 'border-primary text-primary' : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'} w-1/2 py-4 px-1 text-center border-b-2 font-medium text-sm`}
                  onClick={(e) => {
                    e.preventDefault();
                    setActiveTab('registered');
                  }}
                >
                  Registered Events
                </a>
                <a 
                  href="#organized" 
                  className={`${activeTab === 'organized' ? 'border-primary text-primary' : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'} w-1/2 py-4 px-1 text-center border-b-2 font-medium text-sm`}
                  onClick={(e) => {
                    e.preventDefault();
                    setActiveTab('organized');
                  }}
                >
                  Organized Events
                </a>
              </nav>
            </div>
            
            {/* Registered Events Tab Content */}
            <div className={`p-6 ${activeTab !== 'registered' && 'hidden'}`}>
              {loadingRegistered ? (
                <div className="flex justify-center py-6">
                  <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
                </div>
              ) : registeredEvents.length === 0 ? (
                <div className="text-center py-8">
                  <p className="text-gray-500">You haven't registered for any events yet.</p>
                  <Link href="/events">
                    <a className="mt-4 inline-block text-primary hover:text-blue-600">Browse events</a>
                  </Link>
                </div>
              ) : (
                <div className="overflow-hidden">
                  <div className="flow-root">
                    <ul role="list" className="-my-5 divide-y divide-gray-200">
                      {registeredEvents.map((event) => (
                        <li key={event.id} className="py-5">
                          <div className="flex items-center space-x-4">
                            <div className="flex-shrink-0">
                              <img 
                                className="h-16 w-16 rounded-md object-cover" 
                                src={event.image} 
                                alt={event.title} 
                              />
                            </div>
                            <div className="flex-1 min-w-0">
                              <p className="text-lg font-medium text-gray-900 truncate font-poppins">{event.title}</p>
                              <div className="flex items-center space-x-3 mt-1">
                                <div className="flex items-center text-sm text-gray-500">
                                  <i className="far fa-calendar-alt mr-1 text-primary"></i>
                                  <span>{formatEventDate(event.startDate, event.endDate)}</span>
                                </div>
                                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                                  event.status === 'Confirmed' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                                }`}>
                                  {event.status}
                                </span>
                              </div>
                            </div>
                            <div>
                              <Link href={`/event/${event.id}`}>
                                <a className="inline-flex items-center shadow-sm px-3 py-2 border border-gray-300 text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                                  View Details
                                </a>
                              </Link>
                            </div>
                          </div>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              )}
            </div>
            
            {/* Organized Events Tab Content */}
            <div className={`p-6 ${activeTab !== 'organized' && 'hidden'}`}>
              {loadingOrganized ? (
                <div className="flex justify-center py-6">
                  <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
                </div>
              ) : organizedEvents.length === 0 ? (
                <div className="text-center py-8">
                  <p className="text-gray-500">You haven't organized any events yet.</p>
                  <Link href="/organize">
                    <a className="mt-4 inline-block text-primary hover:text-blue-600">Create your first event</a>
                  </Link>
                </div>
              ) : (
                <div className="overflow-hidden">
                  <div className="flow-root">
                    <ul role="list" className="-my-5 divide-y divide-gray-200">
                      {organizedEvents.map((event) => (
                        <li key={event.id} className="py-5">
                          <div className="flex items-center space-x-4">
                            <div className="flex-shrink-0">
                              <img 
                                className="h-16 w-16 rounded-md object-cover" 
                                src={event.image} 
                                alt={event.title} 
                              />
                            </div>
                            <div className="flex-1 min-w-0">
                              <p className="text-lg font-medium text-gray-900 truncate font-poppins">{event.title}</p>
                              <div className="flex items-center space-x-3 mt-1">
                                <div className="flex items-center text-sm text-gray-500">
                                  <i className="far fa-calendar-alt mr-1 text-primary"></i>
                                  <span>{formatEventDate(event.startDate, event.endDate)}</span>
                                </div>
                                <div className="flex items-center text-sm text-gray-500">
                                  <i className="fas fa-user-friends mr-1 text-primary"></i>
                                  <span>{event.participantCount || 0} attendees</span>
                                </div>
                              </div>
                            </div>
                            <div>
                              <Link href={`/event/${event.id}`}>
                                <a className="inline-flex items-center shadow-sm px-3 py-2 border border-gray-300 text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                                  Manage
                                </a>
                              </Link>
                            </div>
                          </div>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="mt-10 text-center">
          <Link href="/organize">
            <a className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-gradient-to-r from-orange-500 to-yellow-500 hover:from-yellow-500 hover:to-orange-500 transition-colors duration-200">
              <i className="fas fa-plus-circle mr-2"></i>
              Create a New Event
            </a>
          </Link>
        </div>
      </div>
    </section>
  );
}
