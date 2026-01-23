import React from "react";

const Home = () => {
  return (
    <div className="page">
      <header className="navbar">
        <div className="logo">picNDel</div>

        <ul className="nav-list" >
          <li>
            <a href="services" >Services</a>
          </li>
          <li>
            <a href="Tracking">Tracking</a>
          </li>
          <li>
            <a href="Prices">Prices</a>
          </li>
          <li>
            <a href="About">About</a>
          </li>

          <li>
            <a href="sign up">sign up</a>
          </li>

          <li>
            <a href="Login">Login</a>
          </li>
        </ul>
      </header>

      <main className="main">
        <section className="left">Left section</section>
        <section className="right">Right section</section>
      </main>

      <footer className="footer">Footer</footer>
    </div>
  );
};

export default Home;
