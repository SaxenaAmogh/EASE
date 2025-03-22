import Navbar from '@/components/Navbar';
import Profile from '@/components/Profile';
import Footer from '@/components/Footer';

export default function ProfilePage() {
  return (
    <div className="font-sans bg-gray-50 min-h-screen">
      <Navbar />
      <div className="pt-16">
        <div className="bg-blue-500 py-16">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h1 className="text-4xl font-extrabold text-white sm:text-5xl font-poppins">
              Your Profile
            </h1>
            <p className="mt-3 max-w-md mx-auto text-xl text-blue-100 sm:text-2xl">
              Manage your information and view your event statistics
            </p>
          </div>
        </div>
        
        <Profile />
        
        <Footer />
      </div>
    </div>
  );
}
