import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatEventDate(startDate: string | Date, endDate: string | Date): string {
  const start = new Date(startDate);
  const end = new Date(endDate);
  
  // Format the dates
  const options: Intl.DateTimeFormatOptions = { 
    month: 'long', 
    day: 'numeric',
    year: 'numeric' 
  };
  
  if (start.toDateString() === end.toDateString()) {
    // Same day event
    return start.toLocaleDateString('en-US', options);
  } else {
    // Multi-day event
    if (start.getMonth() === end.getMonth() && start.getFullYear() === end.getFullYear()) {
      // Same month and year
      return `${start.toLocaleDateString('en-US', { day: 'numeric' })} - ${end.toLocaleDateString('en-US', options)}`;
    } else if (start.getFullYear() === end.getFullYear()) {
      // Different month, same year
      return `${start.toLocaleDateString('en-US', { month: 'long', day: 'numeric' })} - ${end.toLocaleDateString('en-US', options)}`;
    } else {
      // Different years
      return `${start.toLocaleDateString('en-US', options)} - ${end.toLocaleDateString('en-US', options)}`;
    }
  }
}
