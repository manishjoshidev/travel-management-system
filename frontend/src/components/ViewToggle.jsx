export default function ViewToggle({ view, setView }) {
  return (
    <div style={{ marginBottom: "16px" }}>
      <button
        onClick={() => setView("grid")}
        disabled={view === "grid"}
      >
        Grid View
      </button>
      <button
        onClick={() => setView("tile")}
        disabled={view === "tile"}
        style={{ marginLeft: "8px" }}
      >
        Tile View
      </button>
    </div>
  );
}
