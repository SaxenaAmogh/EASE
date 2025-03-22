import { useQuery } from '@tanstack/react-query';
import { User } from '@shared/schema';
import { apiRequest } from '@/lib/queryClient';
import { useToast } from '@/hooks/use-toast';

export default function Profile() {
  const { toast } = useToast();

  const { data: user, isLoading: loadingUser } = useQuery<User>({
    queryKey: ['/api/users/1'],
  });

  const { data: stats, isLoading: loadingStats } = useQuery<{
    eventsAttended: number;
    eventsOrganized: number;
    certificatesEarned: number;
    awardsWon: number;
  }>({
    queryKey: ['/api/dashboard/stats/1'],
  });

  const downloadAIReport = async () => {
    try {
      const response = await apiRequest('GET', '/api/users/1/ai-report', undefined);
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.style.display = 'none';
      a.href = url;
      a.download = 'ai-event-report.txt';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);

      toast({
        title: "Report Downloaded",
        description: "Your AI event analysis report has been downloaded successfully.",
      });
    } catch (error) {
      toast({
        title: "Download Failed",
        description: "There was an error downloading your report. Please try again.",
        variant: "destructive",
      });
    }
  };

  if (loadingUser || loadingStats) {
    return (
      <section id="profile" className="py-12 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex justify-center items-center min-h-[400px]">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
        </div>
      </section>
    );
  }

  if (!user || !stats) {
    return (
      <section id="profile" className="py-12 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-3xl font-extrabold text-gray-900 sm:text-4xl font-poppins">
              Profile Not Found
            </h2>
            <p className="mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4">
              There was an error loading the profile data. Please try again later.
            </p>
          </div>
        </div>
      </section>
    );
  }

  return (
    <section id="profile" className="py-12 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center">
          <h2 className="text-3xl font-extrabold text-gray-900 sm:text-4xl font-poppins">
            Your Profile
          </h2>
          <p className="mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4">
            Manage your personal information and download your event analytics.
          </p>
        </div>

        <div className="mt-12 bg-white shadow rounded-lg overflow-hidden">
          <div className="px-4 py-5 sm:px-6 bg-gray-50">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <img 
                  className="h-16 w-16 rounded-full object-cover" 
                  src={user.profileImage} 
                  alt="Profile" 
                />
              </div>
              <div className="ml-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900 font-poppins">{user.fullname}</h3>
                <p className="text-sm text-gray-500">
                  Member since {new Date(user.memberSince).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
                </p>
              </div>
              <div className="ml-auto">
                <button className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
                  <i className="fas fa-edit mr-2"></i>
                  Edit Profile
                </button>
              </div>
            </div>
          </div>
          <div className="border-t border-gray-200">
            <dl>
              <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                <dt className="text-sm font-medium text-gray-500">
                  Full name
                </dt>
                <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                  {user.fullname}
                </dd>
              </div>
              <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                <dt className="text-sm font-medium text-gray-500">
                  Email address
                </dt>
                <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                  {user.email}
                </dd>
              </div>
              <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                <dt className="text-sm font-medium text-gray-500">
                  Phone number
                </dt>
                <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                  {user.phone}
                </dd>
              </div>
              <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                <dt className="text-sm font-medium text-gray-500">
                  Location
                </dt>
                <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                  {user.location}
                </dd>
              </div>
              <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                <dt className="text-sm font-medium text-gray-500">
                  About
                </dt>
                <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                  {user.about}
                </dd>
              </div>
              <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                <dt className="text-sm font-medium text-gray-500">
                  AI Event Analysis
                </dt>
                <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                  <div className="bg-white px-4 py-4 border border-gray-200 rounded-md">
                    <div className="flex items-center justify-between">
                      <div>
                        <h4 className="text-base font-medium text-gray-900">Your Personalized Event Analysis</h4>
                        <p className="mt-1 text-sm text-gray-500">AI-generated insights based on your event participation and interests.</p>
                      </div>
                      <button 
                        onClick={downloadAIReport}
                        className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary"
                      >
                        <i className="fas fa-download mr-2"></i>
                        Download Report
                      </button>
                    </div>
                    <div className="mt-4">
                      <div className="relative pt-1">
                        <div className="flex mb-2 items-center justify-between">
                          <div>
                            <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded-full text-primary bg-blue-200">
                              Report Generation
                            </span>
                          </div>
                          <div className="text-right">
                            <span className="text-xs font-semibold inline-block text-primary">
                              100%
                            </span>
                          </div>
                        </div>
                        <div className="overflow-hidden h-2 mb-4 text-xs flex rounded bg-blue-200">
                          <div style={{ width: '100%' }} className="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-primary"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </dd>
              </div>
            </dl>
          </div>
        </div>

        <div className="mt-8">
          <h3 className="text-lg font-medium text-gray-900 font-poppins">Event Statistics</h3>
          <div className="mt-4 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0 bg-primary rounded-md p-3">
                    <i className="fas fa-calendar-check text-white"></i>
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">
                        Events Attended
                      </dt>
                      <dd>
                        <div className="text-lg font-medium text-gray-900">
                          {stats?.eventsAttended || 0}
                        </div>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0 bg-orange-500 rounded-md p-3">
                    <i className="fas fa-users text-white"></i>
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">
                        Events Organized
                      </dt>
                      <dd>
                        <div className="text-lg font-medium text-gray-900">
                          {stats?.eventsOrganized || 0}
                        </div>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0 bg-green-500 rounded-md p-3">
                    <i className="fas fa-certificate text-white"></i>
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">
                        Certificates Earned
                      </dt>
                      <dd>
                        <div className="text-lg font-medium text-gray-900">
                          {stats.certificatesEarned}
                        </div>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>

            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <div className="flex items-center">
                  <div className="flex-shrink-0 bg-purple-500 rounded-md p-3">
                    <i className="fas fa-medal text-white"></i>
                  </div>
                  <div className="ml-5 w-0 flex-1">
                    <dl>
                      <dt className="text-sm font-medium text-gray-500 truncate">
                        Awards Won
                      </dt>
                      <dd>
                        <div className="text-lg font-medium text-gray-900">
                          {stats.awardsWon}
                        </div>
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}