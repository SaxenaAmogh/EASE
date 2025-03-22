import Navbar from '@/components/Navbar';
import Home from '@/components/Home';
import About from '@/components/About';
import FeaturedEvents from '@/components/FeaturedEvents';

export default function HomePage() {
  return (
    <div className="font-sans bg-gray-50 min-h-screen">
      <Navbar />
      <div className="pt-16">
        <Home />
        <About />
        <FeaturedEvents />
      </div>
    </div>
  );
}
