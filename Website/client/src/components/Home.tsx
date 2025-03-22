import { Link } from 'wouter';

export default function Home() {
  return (
    <section id="home" className="relative bg-white overflow-hidden">
      <div className="max-w-7xl mx-auto">
        <div className="relative z-10 pb-8 bg-white sm:pb-16 md:pb-20 lg:max-w-2xl lg:w-full lg:pb-28 xl:pb-32">
          <svg className="hidden lg:block absolute right-0 inset-y-0 h-full w-48 text-white transform translate-x-1/2" fill="currentColor" viewBox="0 0 100 100" preserveAspectRatio="none" aria-hidden="true">
            <polygon points="50,0 100,0 50,100 0,100" />
          </svg>

          <div className="relative pt-6 px-4 sm:px-6 lg:px-8"></div>

          <main className="mt-10 mx-auto max-w-7xl px-4 sm:mt-12 sm:px-6 md:mt-16 lg:mt-20 lg:px-8 xl:mt-28">
            <div className="sm:text-center lg:text-left">
              <h1 className="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl font-poppins">
                <span className="block xl:inline">Event Administration</span>
                <span className="block text-primary xl:inline"> Solutions for Everyone</span>
              </h1>
              <p className="mt-3 text-base text-gray-500 sm:mt-5 sm:text-lg sm:max-w-xl sm:mx-auto md:mt-5 md:text-xl lg:mx-0">
                Simplify your event management process with our comprehensive platform. Organize, manage, and attend events with ease.
              </p>
              <div className="mt-5 sm:mt-8 sm:flex sm:justify-center lg:justify-start">
                <div className="rounded-md shadow">
                  <Link 
                    href="/events"
                    className="w-full flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-md text-white bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-500 md:py-4 md:text-lg md:px-10"
                  >
                    Attend an Event
                  </Link>
                </div>
                <div className="mt-3 sm:mt-0 sm:ml-3">
                  <Link 
                    href="/organize"
                    className="w-full flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-md text-blue-500 bg-gray-50 hover:bg-gray-100 md:py-4 md:text-lg md:px-10"
                  >
                    Organize an Event
                  </Link>
                </div>
              </div>
            </div>
          </main>
        </div>
      </div>
      <div className="lg:absolute lg:inset-y-0 lg:right-0 lg:w-1/2">
        <div className="h-56 w-full sm:h-72 md:h-96 lg:w-full lg:h-full relative">
          <div className="w-full h-full bg-gradient-to-br from-blue-100 to-purple-100 flex items-center justify-center animate-[float_6s_ease-in-out_infinite]">
            <img 
              src="https://images.unsplash.com/photo-1595761139970-5a38c3e38e4f?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" 
              alt="3D event visualization" 
              className="absolute w-full h-full object-cover object-center opacity-60" 
            />
            
            {/* 3D Objects/Elements Overlay */}
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="w-32 h-32 bg-gradient-to-r from-blue-500/50 to-blue-600/50 rounded-full flex items-center justify-center shadow-lg transform -translate-x-20 translate-y-10 animate-pulse">
                <i className="fas fa-calendar-alt text-white text-4xl"></i>
              </div>
              <div className="w-24 h-24 bg-gradient-to-r from-orange-500/50 to-yellow-400/50 rounded-lg flex items-center justify-center shadow-lg transform translate-x-24 -translate-y-20 animate-pulse" style={{ animationDelay: '1s' }}>
                <i className="fas fa-users text-white text-3xl"></i>
              </div>
              <div className="w-20 h-20 bg-gradient-to-r from-purple-500/50 to-pink-500/50 rounded-lg flex items-center justify-center shadow-lg transform translate-x-10 translate-y-24 animate-pulse" style={{ animationDelay: '2s' }}>
                <i className="fas fa-map-marker-alt text-white text-2xl"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
