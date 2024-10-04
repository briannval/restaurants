import { useEffect } from 'react'
import './App.css'
import MapComponent from './MapComponent'
import axios from 'axios'

function App() {

  useEffect(() => {
    const fetchPlaces = async () => {
      let res = await axios.get("http://localhost:8080/restaurants")
      console.log(res)
    }
    fetchPlaces();
  }, [])

  return (
    <>
      <div className="flex items-center w-full justify-center min-h-screen">
        <MapComponent />
      </div>
    </>
  )
}

export default App
