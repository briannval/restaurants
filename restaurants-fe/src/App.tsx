import { useEffect } from "react";
import { GoogleMap, useLoadScript, Marker } from "@react-google-maps/api";
import axios from 'axios';
import './App.css';
import { Card, CardContent, CardHeader, CardTitle } from "./components/ui/card";
import { Button } from "./components/ui/button";
import { useDispatch, useSelector } from 'react-redux';
import { RootState, setLocation, setRecommend, setRecommendedRestaurants, setRestaurants, setSelectedRestaurant } from "./store";

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

function App() {
  const dispatch = useDispatch();
  const location = useSelector((state: RootState) => state.restaurant.location);
  const restaurants = useSelector((state: RootState) => state.restaurant.restaurants);
  const selectedRestaurant = useSelector((state: RootState) => state.restaurant.selectedRestaurant);
  const recommend = useSelector((state: RootState) => state.restaurant.recommend);
  const recommendedRestaurants = useSelector((state: RootState) => state.restaurant.recommendedRestaurants);

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY
  });

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          dispatch(setLocation({
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          }));
        },
        () => {
          console.error("Geolocation permission denied or unavailable");
          dispatch(setLocation(defaultCenter));
        }
      );
    }
    fetchRestaurants();
  }, []);

  const fetchRestaurants = async () => {
    try {
      dispatch(setSelectedRestaurant(null));
      dispatch(setRestaurants(null));
      let res = await axios.get("http://localhost:8080/restaurants");
      dispatch(setRestaurants(res.data));
      console.log("test")
    } catch (error) {
      console.error("Error fetching places:", error);
    }
  }

  const recommendRestaurants = async () => {
    try {
      dispatch(setRecommend(true));
      dispatch(setSelectedRestaurant(null));
      let res = await axios.post("http://localhost:8080/restaurants/recommend", {
        latitude: location?.lat,
        longitude: location?.lng,
      });
      console.log(res.data)
      dispatch(setRecommendedRestaurants(res.data));
    } catch (error) {
      //
    }
  }

  const addRestaurant = async (i: number) => {
    try {
      let newR = recommendedRestaurants![i];
      await axios.post("http://localhost:8080/restaurants", {
        name: newR.name,
        latitude: newR.latitude,
        longitude: newR.longitude,
        address: newR.address,
        googlePlaceId: newR.googlePlaceId,
      });
      let modifiedRecommended = recommendedRestaurants!.filter((_, idx) => idx != i);
      dispatch(setRecommendedRestaurants(modifiedRecommended));
    } catch (error) {
      //
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
          <Button onClick={recommendRestaurants} variant={"default"} className="bg-black hover:bg-gray-800 text-white mt-4 rounded-xl mr-4">
            Recommend
          </Button>
        </CardContent>
      </Card>
      <Card className="w-full max-w-xl h-[600px] shadow-lg">
        <CardHeader>
          <CardTitle className="text-center text-2xl font-bold">{recommend ? "Recommended Near You" : selectedRestaurant ? selectedRestaurant.name : "Restaurants"}</CardTitle>
        </CardHeader>
        <CardContent>
          {recommend ?
            !recommendedRestaurants ? <p>Fetching recommendations...</p> :
              <>
                <div className="h-[430px] overflow-y-auto">
                  {recommendedRestaurants!.map((restaurant, idx) => (
                    <div onClick={() => addRestaurant(idx)} key={idx} className="mb-4 bg-white hover:bg-gray-300 bg-rounded-xl">
                      <h3 className="font-medium text-lg">{restaurant.name}</h3>
                      <p className="text-sm text-gray-600">{restaurant.address}</p>
                    </div>
                  ))}
                </div>
                <Button onClick={() => {
                  dispatch(setRecommendedRestaurants(null));
                  dispatch(setRecommend(false))
                  fetchRestaurants();
                }} variant={"default"} className="bg-black hover:bg-gray-800 text-white mt-4 rounded-xl mr-4">
                  Exit
                </Button>
              </>
            : selectedRestaurant ?
              <>
                <Button variant={"default"} className="bg-white border hover:bg-gray-300 text-black mt-4 rounded-xl mr-4">
                  Add Review
                </Button>
                <Button onClick={() => dispatch(setSelectedRestaurant(null))} variant={"default"} className="bg-black hover:bg-gray-800 text-white mt-4 rounded-xl mr-4">
                  Back
                </Button>
              </>
              : restaurants ? (
                <>
                  <div className="h-[430px]">
                    {restaurants.map((restaurant) => (
                      <div onClick={() => dispatch(setSelectedRestaurant(restaurant))} key={restaurant.id} className="mb-4 bg-white hover:bg-gray-300 bg-rounded-xl">
                        <h3 className="font-medium text-lg">{restaurant.name}</h3>
                        <p className="text-sm text-gray-600">{restaurant.address}</p>
                      </div>
                    ))}
                  </div>
                  <Button onClick={() => fetchRestaurants()} variant={"default"} className="bg-black hover:bg-gray-800 text-white mt-4 rounded-xl mr-4">
                    Refresh
                  </Button>
                </>
              ) : (
                <p>Loading restaurants...</p>
              )}
        </CardContent>
      </Card>
    </div>
  );
}

export default App;
