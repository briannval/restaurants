import { configureStore, createSlice, PayloadAction } from '@reduxjs/toolkit';

interface RestaurantRecommend { 
  name: string;
  address: string;
  googlePlaceId: string;
  latitude: number;
  longitude: number;
}

interface Restaurant extends RestaurantRecommend {
  id: number;
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
  recommend: boolean;
  recommendedRestaurants: RestaurantRecommend[] | null;
}

const initialState: RestaurantState = {
  selectedRestaurant: null,
  location: null,
  restaurants: null,
  recommend: false,
  recommendedRestaurants: null
};

const restaurantSlice = createSlice({
  name: 'restaurant',
  initialState,
  reducers: {
    setSelectedRestaurant: (state, action: PayloadAction<Restaurant | null>) => {
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
    setRestaurants: (state, action: PayloadAction<Restaurant[] | null>) => {
      state.restaurants = action.payload;
    },
    setRecommend: (state, action: PayloadAction<boolean>) => {
    state.recommend = action.payload;
    },
    setRecommendedRestaurants: (state, action: PayloadAction<RestaurantRecommend[] | null>) => {
    state.recommendedRestaurants = action.payload;
    }
  },
});

export const {
  setSelectedRestaurant,
  addReview,
  setLocation,
  setRestaurants,
  setRecommend,
  setRecommendedRestaurants
} = restaurantSlice.actions;

const store = configureStore({
    reducer: {
        restaurant: restaurantSlice.reducer
    }
})

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;
