export default function About() {
  return (
    <section className="py-12 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="lg:text-center">
          <h2 className="text-base text-primary font-semibold tracking-wide uppercase font-poppins">Our Platform</h2>
          <p className="mt-2 text-3xl leading-8 font-extrabold tracking-tight text-gray-900 sm:text-4xl font-poppins">
            A better way to manage events
          </p>
          <p className="mt-4 max-w-2xl text-xl text-gray-500 lg:mx-auto">
            EASE provides comprehensive tools for both event organizers and attendees, making the entire experience seamless and efficient.
          </p>
        </div>

        <div className="mt-10">
          <div className="space-y-10 md:space-y-0 md:grid md:grid-cols-2 md:gap-x-8 md:gap-y-10">
            <div className="flex">
              <div className="flex-shrink-0">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-primary text-white">
                  <i className="fas fa-calendar-check"></i>
                </div>
              </div>
              <div className="ml-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900 font-poppins">Easy Event Creation</h3>
                <p className="mt-2 text-base text-gray-500">
                  Create and customize events in minutes with our intuitive interface. Add details, set schedules, and manage registrations effortlessly.
                </p>
              </div>
            </div>

            <div className="flex">
              <div className="flex-shrink-0">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-primary text-white">
                  <i className="fas fa-ticket-alt"></i>
                </div>
              </div>
              <div className="ml-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900 font-poppins">Streamlined Registration</h3>
                <p className="mt-2 text-base text-gray-500">
                  Simplify the registration process for attendees with personalized forms, automatic confirmations, and instant access to event details.
                </p>
              </div>
            </div>

            <div className="flex">
              <div className="flex-shrink-0">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-primary text-white">
                  <i className="fas fa-chart-line"></i>
                </div>
              </div>
              <div className="ml-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900 font-poppins">Comprehensive Analytics</h3>
                <p className="mt-2 text-base text-gray-500">
                  Track attendance, engagement, and other key metrics with our powerful analytics dashboard. Get AI-powered insights for future events.
                </p>
              </div>
            </div>

            <div className="flex">
              <div className="flex-shrink-0">
                <div className="flex items-center justify-center h-12 w-12 rounded-md bg-primary text-white">
                  <i className="fas fa-users"></i>
                </div>
              </div>
              <div className="ml-4">
                <h3 className="text-lg leading-6 font-medium text-gray-900 font-poppins">Community Building</h3>
                <p className="mt-2 text-base text-gray-500">
                  Foster connections between attendees with built-in networking features, discussion forums, and collaborative tools.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
