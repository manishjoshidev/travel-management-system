export default function Pagination({ page, totalPages, setPage }) {
  return (
    <div style={{ marginTop: "25px", textAlign: "center" }}>
      <button disabled={page === 0} onClick={() => setPage(page - 1)}>
        Prev
      </button>

      <span style={{ margin: "0 15px" }}>
        Page {page + 1} of {totalPages}
      </span>

      <button
        disabled={page + 1 >= totalPages}
        onClick={() => setPage(page + 1)}
      >
        Next
      </button>
    </div>
  );
}
