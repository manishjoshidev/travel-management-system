import NavigationBar from "./components/NavigationBar.jsx";
import Footer from "./components/Footer.jsx";
import "./App.css";

export default function App() {
  return (
    <div className="main" id="main">
      <div className="header">
        <NavigationBar className="navigationbar" />
      </div>
      <div className="mainBody" id="mainBody"></div>
      <div className="footer" id="footer">
        <Footer className="footer" />
      </div>
    </div>
  );
}
