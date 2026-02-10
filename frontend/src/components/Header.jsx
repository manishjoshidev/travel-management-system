export default function Header({ role, setRole }) {
  return (
    <div className="controls">
      <select value={role} onChange={e => setRole(e.target.value)}>
        <option value="EMPLOYEE">Employee</option>
        <option value="ADMIN">Admin</option>
      </select>
    </div>
  );
}
