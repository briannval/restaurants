import { useState, useEffect } from "react";
import { GoogleMap, useLoadScript, Marker } from "@react-google-maps/api";
import axios from 'axios';
import './App.css';
import { Card, CardContent, CardHeader, CardTitle } from "./components/ui/card";
import { Button } from "./components/ui/button";
import { ScrollArea } from "./components/ui/scroll-area";

const mapContainerStyle = {
  width: "100%",
  height: "420px",
};

const options = {
  disableDefaultUI: true,
  zoomControl: true,
};

const defaultCenter = {
  lat: -34.397,
  lng: 150.644,
};

interface Restaurant {
  name: string;
  address: string;
  googlePlaceId: string;
  id: number;
  latitude: number;
  longitude: number;
  reviews: Review[];
}

interface Review {
  id: number;
  taste: string;
  service: string;
  comment: string;
}

function App() {
  const [location, setLocation] = useState<google.maps.LatLngLiteral | null>(null);
  const [restaurants, setRestaurants] = useState<Restaurant[] | null>(null);

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY
  });

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLocation({
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          });
        },
        () => {
          console.error("Geolocation permission denied or unavailable");
          setLocation(defaultCenter);
        }
      );
    }
    fetchPlaces();
  }, []);

  const fetchPlaces = async () => {
    try {
      let res = await axios.get("http://localhost:8080/restaurants");
      setRestaurants(res.data);
      console.log(restaurants);
    } catch (error) {
      console.error("Error fetching places:", error);
    }
  }

  if (loadError) return <div>Error loading maps</div>;
  if (!isLoaded || !location) return <div>Loading Maps...</div>;

  return (
    <div className="flex items-center w-full justify-center min-h-screen space-x-6">
      <Card className="w-full max-w-xl h-[600px] shadow-lg">
        <CardHeader>
          <CardTitle className="text-center text-2xl font-bold">Your Location</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="mb-6">
            <GoogleMap
              mapContainerStyle={mapContainerStyle}
              zoom={14}
              center={location}
              options={options}
            >
              {location && <Marker position={location} />}
            </GoogleMap></div>
          <Button variant={"default"} className="bg-black hover:bg-gray-800 text-white mt-4 rounded-xl mr-4">
            Recommend
          </Button>
        </CardContent>
      </Card>
      <Card className="w-full max-w-xl h-[600px] shadow-lg">
        <CardHeader>
          <CardTitle className="text-center text-2xl font-bold">Restaurants</CardTitle>
        </CardHeader>
        <CardContent>
          {restaurants ? (
            restaurants.map((restaurant) => (
              <div key={restaurant.id} className="mb-4 bg-white hover:bg-gray-300 bg-rounded-xl">
                <h3 className="font-medium text-lg">{restaurant.name}</h3>
                <p className="text-sm text-gray-600">{restaurant.address}</p>
              </div>
            ))
          ) : (
            <p>Loading restaurants...</p>
          )}
        </CardContent>
      </Card>

    </div>
  );
}

export default App;
