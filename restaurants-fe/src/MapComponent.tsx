import React, { useState, useEffect } from "react";
import { GoogleMap, useLoadScript, Marker } from "@react-google-maps/api";
import { Card, CardContent, CardHeader, CardTitle } from "./components/ui/card";

const mapContainerStyle = {
    width: "100%",   // Full width of the card
    height: "200px", // Adjust this based on the height you want for the map
};

const options = {
    disableDefaultUI: true,
    zoomControl: true,
};

const defaultCenter = {
    lat: -34.397,
    lng: 150.644,
};

const MapComponent: React.FC = () => {
    const [location, setLocation] = useState<google.maps.LatLngLiteral | null>(null);

    // Load Google Maps script
    const { isLoaded, loadError } = useLoadScript({
        googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY, // Your Google Maps API key
    });

    // Get user's location
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
                    setLocation(defaultCenter); // Fallback
                }
            );
        }
    }, []);

    if (loadError) return <div>Error loading maps</div>;
    if (!isLoaded || !location) return <div>Loading Maps...</div>;

    return (
        <div className="flex items-center w-full justify-center min-h-screen">
            <Card className="w-full max-w-xl shadow-lg">
                <CardHeader>
                    <CardTitle className="text-center text-xl font-bold mb-4">Your Location</CardTitle>
                </CardHeader>
                <CardContent>
                    <GoogleMap
                        mapContainerStyle={mapContainerStyle}
                        zoom={12}
                        center={location}
                        options={options}
                    >
                        {location && <Marker position={location} />}
                    </GoogleMap>
                </CardContent>
            </Card>
        </div>
    );
};

export default MapComponent;
