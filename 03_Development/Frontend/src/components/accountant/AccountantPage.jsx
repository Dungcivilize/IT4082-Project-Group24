import React, { useEffect, useState } from 'react';
import './AccountantPage.css';

function AccountantPage() {
  const [periods, setPeriods] = useState([]);
  const [selectedPeriodId, setSelectedPeriodId] = useState(null);
  const [selectedPeriodDetails, setSelectedPeriodDetails] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/accountant/payment-periods')
      .then(res => res.json())
      .then(data => setPeriods(data))
      .catch(console.error);
  }, []);

  useEffect(() => {
    if (!selectedPeriodId) {
      setSelectedPeriodDetails(null);
      return;
    }

    fetch(`http://localhost:8080/api/accountant/payment-periods/${selectedPeriodId}/details`)
      .then(res => res.json())
      .then(data => setSelectedPeriodDetails(data))
      .catch(console.error);
  }, [selectedPeriodId]);

  return (
    <div className="accountant-container">
      <h1 className="accountant-title">Danh sách đợt thu phí</h1>

      <ul className="period-list">
        {periods.map(period => (
          <li key={period.paymentPeriodId}>
            <button
              className={`period-button ${selectedPeriodId === period.paymentPeriodId ? 'active' : ''}`}
              onClick={() => setSelectedPeriodId(period.paymentPeriodId)}
            >
              Tháng {period.month} năm {period.year}
            </button>
          </li>
        ))}
      </ul>

      {selectedPeriodDetails && (
        <div className="period-details">
          <h2 className="period-subtitle">
            Chi tiết đợt thu phí: Tháng {selectedPeriodDetails.month} năm {selectedPeriodDetails.year}
          </h2>

          {selectedPeriodDetails.apartments?.map(apt => (
            <div key={apt.apartmentId} className="apartment-block">
              <h3 className="apartment-title">Căn hộ: {apt.apartmentCode}</h3>
              <table className="invoice-table">
                <thead>
                  <tr>
                    <th>Tên hóa đơn</th>
                    <th>Loại hóa đơn</th>
                    <th>Số lượng</th>
                    <th>Đơn giá</th>
                    <th>Thành tiền</th>
                    <th>Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  {apt.invoices?.map(inv => (
                    <tr key={inv.paymentDetailId}>
                      <td>{inv.serviceName}</td>
                      <td>{inv.serviceType}</td>
                      <td>{inv.amount}</td>
                      <td>{inv.unitPrice.toLocaleString()}</td>
                      <td>{inv.totalPrice.toLocaleString()}</td>
                      <td>{inv.status}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default AccountantPage;
