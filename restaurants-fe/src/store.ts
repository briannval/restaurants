import { configureStore, createSlice, PayloadAction } from '@reduxjs/toolkit';

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

interface RestaurantState {
  selectedRestaurant: Restaurant | null;
  location: google.maps.LatLngLiteral | null;
  restaurants: Restaurant[] | null;
}

const initialState: RestaurantState = {
  selectedRestaurant: null,
  location: null,
  restaurants: null,
};

const restaurantSlice = createSlice({
  name: 'restaurant',
  initialState,
  reducers: {
    setSelectedRestaurant: (state, action: PayloadAction<Restaurant>) => {
      state.selectedRestaurant = action.payload;
    },
    addReview: (state, action: PayloadAction<{ restaurantId: number; review: Review }>) => {
      const restaurant = state.selectedRestaurant;
      if (restaurant && restaurant.id === action.payload.restaurantId) {
        restaurant.reviews.push(action.payload.review);
      }
    },
    setLocation: (state, action: PayloadAction<google.maps.LatLngLiteral>) => {
      state.location = action.payload;
    },
    setRestaurants: (state, action: PayloadAction<Restaurant[]>) => {
      state.restaurants = action.payload;
    },
  },
});

export const {
  setSelectedRestaurant,
  addReview,
  setLocation,
  setRestaurants,
} = restaurantSlice.actions;

const store = configureStore({
    reducer: {
        restaurant: restaurantSlice.reducer
    }
})

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;
