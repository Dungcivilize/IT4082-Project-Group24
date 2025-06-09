import React, { useEffect, useState } from "react";
import "./PaymentDetailsForPeriod.css";

function PaymentDetailsForPeriod({ paymentPeriodId }) {
  const [periodDetails, setPeriodDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    setLoading(true);
    fetch(`http://localhost:8080/api/accountant/payment-periods/${paymentPeriodId}/details`)
      .then((res) => res.json())
      .then((data) => {
        data.apartments.forEach((apt) =>
          apt.invoices.forEach((inv) => (inv.amountLocal = inv.amount))
        );
        setPeriodDetails(data);
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, [paymentPeriodId]);

  const handleAmountChange = (aptId, paymentDetailId, value) => {
    const newPeriodDetails = { ...periodDetails };
    const apt = newPeriodDetails.apartments.find((a) => a.apartmentId === aptId);
    const invoice = apt.invoices.find((i) => i.paymentDetailId === paymentDetailId);
    invoice.amountLocal = value;
    setPeriodDetails(newPeriodDetails);
  };

  const handleSave = async () => {
    setSaving(true);
    setError(null);

    try {
      for (const apt of periodDetails.apartments) {
        for (const inv of apt.invoices) {
          if (inv.amountLocal !== inv.amount) {
            await fetch(`http://localhost:8080/api/accountant/payment-periods/payment-details/${inv.paymentDetailId}`, {
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ amount: Number(inv.amountLocal) }),
            });
          }
        }
      }
      alert("Cập nhật thành công");
      const res = await fetch(`http://localhost:8080/api/accountant/payment-periods/${paymentPeriodId}/details`);
      const data = await res.json();
      data.apartments.forEach((apt) =>
        apt.invoices.forEach((inv) => (inv.amountLocal = inv.amount))
      );
      setPeriodDetails(data);
    } catch (e) {
      setError("Lỗi khi cập nhật: " + e.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <p className="pd-loading">Đang tải chi tiết đợt thu phí...</p>;
  if (error) return <p className="pd-error">{error}</p>;

  return (
    <div className="pd-container">
      <h2 className="pd-title">
        Chi tiết đợt thu phí: Tháng {periodDetails.month} năm {periodDetails.year}
      </h2>

      {periodDetails.apartments.map((apt) => (
        <div key={apt.apartmentId} className="pd-apartment">
          <h3 className="pd-apartment-code">Căn hộ: {apt.apartmentCode}</h3>
          <table className="pd-table">
            <thead>
              <tr>
                <th>Tên hóa đơn</th>
                <th>Loại hóa đơn</th>
                <th>Số lượng (amount)</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
                <th>Trạng thái</th>
              </tr>
            </thead>
            <tbody>
              {apt.invoices.map((inv) => (
                <tr key={inv.paymentDetailId}>
                  <td>{inv.serviceName}</td>
                  <td>{inv.serviceType}</td>
                  <td>
                    <input
                      type="number"
                      min="0"
                      className="pd-input-amount"
                      value={inv.amountLocal}
                      onChange={(e) =>
                        handleAmountChange(apt.apartmentId, inv.paymentDetailId, e.target.value)
                      }
                      disabled={
                        inv.status.toLowerCase() === "paid" ||
                        inv.serviceType.toLowerCase() === "motorbike" ||
                        inv.serviceType.toLowerCase() === "car"
                      }
                    />
                  </td>
                  <td>{Number(inv.unitPrice).toLocaleString()}</td>
                  <td>{(inv.amountLocal * inv.unitPrice).toLocaleString()}</td>
                  <td>{inv.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ))}

      <button
        className="pd-save-btn"
        onClick={handleSave}
        disabled={saving}
        type="button"
      >
        {saving ? "Đang lưu..." : "Lưu thay đổi"}
      </button>

      {error && <p className="pd-error">{error}</p>}
    </div>
  );
}

export default PaymentDetailsForPeriod;
