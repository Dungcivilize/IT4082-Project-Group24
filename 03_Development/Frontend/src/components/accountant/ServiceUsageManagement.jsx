import React, { useState, useEffect } from 'react';
import { createPaymentDetail } from '../../api/serviceUsage';
import { getAllPaymentPeriods } from '../../api/paymentPeriod';
import { getAllServiceTypes } from '../../api/serviceType';
import { getAllOwnerships } from '../../api/ownership';
import '../../styles/ServiceUsage.css';

const ServiceUsageManagement = () => {
  const [paymentPeriods, setPaymentPeriods] = useState([]);
  const [serviceTypes, setServiceTypes] = useState([]);
  const [ownerships, setOwnerships] = useState([]);
  const [selectedPeriod, setSelectedPeriod] = useState('');
  const [selectedOwnership, setSelectedOwnership] = useState('');
  const [selectedService, setSelectedService] = useState('');
  const [selectedServiceUnit, setSelectedServiceUnit] = useState('');
  const [amount, setAmount] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [periodsData, servicesData, ownershipsData] = await Promise.all([
          getAllPaymentPeriods(),
          getAllServiceTypes(),
          getAllOwnerships(),
        ]);
        setPaymentPeriods(periodsData);
        setServiceTypes(servicesData);
        setOwnerships(ownershipsData);
      } catch (error) {
        setError('Có lỗi khi tải dữ liệu');
      }
    };

    loadData();
  }, []);

  const handleServiceChange = (e) => {
    const serviceId = e.target.value;
    setSelectedService(serviceId);
    const service = serviceTypes.find(
      (s) => s.serviceTypeId.toString() === serviceId
    );
    setSelectedServiceUnit(service?.unit || '');
  };

  const handleAmountChange = (e) => {
    const value = e.target.value;
    if (value === '' || (parseFloat(value) > 0 && !isNaN(value))) {
      setAmount(value);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      if (
        !selectedPeriod ||
        !selectedOwnership ||
        !selectedService ||
        !amount
      ) {
        throw new Error('Vui lòng điền đầy đủ thông tin');
      }

      const amountValue = parseFloat(amount);
      if (amountValue <= 0) {
        throw new Error('Số lượng phải lớn hơn 0');
      }

      const data = {
        paymentPeriodId: parseInt(selectedPeriod),
        ownershipId: parseInt(selectedOwnership),
        serviceTypeId: parseInt(selectedService),
        amount: amountValue,
      };

      await createPaymentDetail(data);
      setSuccess('Tạo chi tiết thanh toán thành công');

      // Reset form
      setSelectedService('');
      setAmount('');
      setSelectedServiceUnit('');
    } catch (error) {
      setError(error.response?.data?.message || error.message);
    } finally {
      setLoading(false);
    }
  };

  const getPaymentPeriodStatus = (period) => {
    return period.completed ? ' (Đã kết thúc)' : ' (Đang diễn ra)';
  };

  return (
    <div className="service-usage-container">
      <h2>Nhập số lượng sử dụng dịch vụ</h2>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      <form onSubmit={handleSubmit} className="service-usage-form">
        <div className="form-group">
          <label>Kỳ thu phí:</label>
          <select
            value={selectedPeriod}
            onChange={(e) => setSelectedPeriod(e.target.value)}
            required
          >
            <option value="">Chọn kỳ thu phí</option>
            {paymentPeriods.map((period) => (
              <option
                key={period.paymentPeriodId}
                value={period.paymentPeriodId}
                className={period.completed ? 'expired-period' : ''}
              >
                {`Tháng ${period.month}/${period.year}${getPaymentPeriodStatus(
                  period
                )}`}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Căn hộ:</label>
          <select
            value={selectedOwnership}
            onChange={(e) => setSelectedOwnership(e.target.value)}
            required
          >
            <option value="">Chọn căn hộ</option>
            {ownerships.map((ownership) => (
              <option key={ownership.ownershipId} value={ownership.ownershipId}>
                {`${ownership.apartmentCode}`}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Loại dịch vụ:</label>
          <select
            value={selectedService}
            onChange={handleServiceChange}
            required
          >
            <option value="">Chọn dịch vụ</option>
            {serviceTypes.map((service) => (
              <option key={service.serviceTypeId} value={service.serviceTypeId}>
                {`${service.serviceName} - ${service.unitPrice.toLocaleString(
                  'vi-VN'
                )}đ`}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Số lượng sử dụng:</label>
          <div className="input-with-unit">
            <input
              type="number"
              value={amount}
              onChange={handleAmountChange}
              min="0.01"
              step="0.01"
              required
              placeholder="Nhập số lượng"
            />
            {selectedServiceUnit && (
              <span className="unit">{selectedServiceUnit}</span>
            )}
          </div>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Đang xử lý...' : 'Tạo chi tiết thanh toán'}
        </button>
      </form>
    </div>
  );
};

export default ServiceUsageManagement;
