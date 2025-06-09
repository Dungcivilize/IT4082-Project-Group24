import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // ⬅️ thêm dòng này

const CreatePaymentPeriod = () => {
  const [month, setMonth] = useState('');
  const [year, setYear] = useState('');
  const [note, setNote] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState(null);
  const navigate = useNavigate(); // ⬅️ thêm dòng này

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);

    if (!month || !year) {
      setMessage('Vui lòng nhập tháng và năm');
      return;
    }

    setLoading(true);

    try {
const res = await fetch('http://localhost:8080/api/accountant/payment-periods', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    month: parseInt(month, 10),
    year: parseInt(year, 10),
    note: note.trim(),
  }),
});


      if (!res.ok) {
        throw new Error('Lỗi khi tạo đợt thu phí');
      }

      const data = await res.json();
      navigate(`/accountant/payment-periods/${data.paymentPeriodId}/edit`); // ⬅️ điều hướng sang trang chỉnh sửa
    } catch (error) {
      setMessage(error.message || 'Có lỗi xảy ra');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '2rem auto', padding: 20, border: '1px solid #ddd', borderRadius: 6 }}>
      <h2>Tạo Đợt Thu Phí Mới</h2>
      {message && <p>{message}</p>}
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 12 }}>
          <label>Tháng:</label>
          <input
            type="number"
            value={month}
            onChange={e => setMonth(e.target.value)}
            min="1"
            max="12"
            required
          />
        </div>
        <div style={{ marginBottom: 12 }}>
          <label>Năm:</label>
          <input
            type="number"
            value={year}
            onChange={e => setYear(e.target.value)}
            min="2000"
            max="2100"
            required
          />
        </div>
        <div style={{ marginBottom: 12 }}>
          <label>Ghi chú:</label>
          <textarea
            value={note}
            onChange={e => setNote(e.target.value)}
            rows="3"
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Đang tạo...' : 'Tạo đợt thu phí'}
        </button>
      </form>
    </div>
  );
};

export default CreatePaymentPeriod;
