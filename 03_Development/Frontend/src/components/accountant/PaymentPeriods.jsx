import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CreatePaymentPeriod from './CreatePaymentPeriod';
import PaymentDetailsForPeriod from './PaymentDetailsForPeriod';
import './PaymentPeriods.css';

function PaymentPeriods() {
  const [periods, setPeriods] = useState([]);
  const [selectedPeriod, setSelectedPeriod] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/api/accountant/payment-periods')
      .then(res => res.json())
      .then(data => setPeriods(data))
      .catch(console.error);
  }, []);

  const handleCreatedPeriod = (newPeriod) => {
    setPeriods(prev => {
      const exists = prev.some(p => p.paymentPeriodId === newPeriod.paymentPeriodId);
      if (exists) return prev;
      return [newPeriod, ...prev];
    });
    setSelectedPeriod(newPeriod);
  };

  return (
    <div className="payment-periods-container">
      <h1 className="payment-periods-title">Danh sách đợt thu phí</h1>

      {/* Thanh chứa 2 nút (Xem thanh toán + Tạo đợt thu phí) */}
      <div className="action-bar">
        <button
          onClick={() => navigate('/accountant/processing-payments')}
          className="navigate-button"
        >
          📄 Xem thanh toán đang xử lý
        </button>

        <CreatePaymentPeriod onCreated={handleCreatedPeriod} />
      </div>

      {/* Danh sách đợt thu phí */}
      <ul className="periods-list">
        {periods.map(period => (
          <li key={period.paymentPeriodId} className="periods-list-item">
            <button
              className={`period-button ${
                selectedPeriod?.paymentPeriodId === period.paymentPeriodId ? 'selected' : ''
              }`}
              onClick={() => setSelectedPeriod(period)}
            >
              Tháng {period.month} năm {period.year}
            </button>
          </li>
        ))}
      </ul>

      {/* Hiển thị chi tiết đợt thu đã chọn */}
      {selectedPeriod && (
        <PaymentDetailsForPeriod paymentPeriodId={selectedPeriod.paymentPeriodId} />
      )}
    </div>
  );
}

export default PaymentPeriods;
