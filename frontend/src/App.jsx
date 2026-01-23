import "./App.css";
import Home from "./components/Home";
import SignUpPage from "./components/signupPage/SignUpPage";
import { Routes, Route, Link } from "react-router-dom";

function App() {
  return (
     <>
    <Link to="/Home" >Home </Link> 
    <Link to="/SignUpPage" >SignUpPage </Link> 


    <Routes>
      <Route path="/Home" element={<Home />} />
      <Route path="/signup" element={<SignUpPage/>} />
      
    </Routes>
    </>
  );
}

export default App;
