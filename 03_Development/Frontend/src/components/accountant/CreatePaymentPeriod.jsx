import React, { useState } from "react";
import "./CreatePaymentPeriod.css";

function CreatePaymentPeriod({ onCreated }) {
  const [month, setMonth] = useState("");
  const [year, setYear] = useState("");
  const [note, setNote] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const res = await fetch("http://localhost:8080/api/accountant/payment-periods", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ month: Number(month), year: Number(year), note }),
      });

      if (!res.ok) {
        const errData = await res.json();
        throw new Error(errData.message || "Tạo đợt thu phí thất bại");
      }

      const data = await res.json();
      onCreated(data);
      setMonth("");
      setYear("");
      setNote("");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="cpp-form">
      <h2 className="cpp-title">Tạo đợt thu phí mới</h2>

      <div className="cpp-field">
        <label className="cpp-label" htmlFor="month-input">Tháng:</label>
        <input
          id="month-input"
          className="cpp-input"
          type="number"
          min="1"
          max="12"
          value={month}
          onChange={(e) => setMonth(e.target.value)}
          required
        />
      </div>

      <div className="cpp-field">
        <label className="cpp-label" htmlFor="year-input">Năm:</label>
        <input
          id="year-input"
          className="cpp-input"
          type="number"
          min="2000"
          max="2100"
          value={year}
          onChange={(e) => setYear(e.target.value)}
          required
        />
      </div>

      <div className="cpp-field">
        <label className="cpp-label" htmlFor="note-input">Ghi chú:</label>
        <input
          id="note-input"
          className="cpp-input"
          type="text"
          value={note}
          onChange={(e) => setNote(e.target.value)}
        />
      </div>

      <button type="submit" className="cpp-submit-btn" disabled={loading}>
        {loading ? "Đang tạo..." : "Tạo đợt thu phí"}
      </button>

      {error && <p className="cpp-error">{error}</p>}
    </form>
  );
}

export default CreatePaymentPeriod;
