import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../../styles/ServiceType.css';

const API_URL = "http://localhost:8080/api";

const ServiceTypeManagement = () => {
  const [serviceTypes, setServiceTypes] = useState([]);
  const [selectedServiceType, setSelectedServiceType] = useState(null);
  const [formData, setFormData] = useState({
    serviceName: '',
    serviceType: 'electricity',
    unitPrice: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadServiceTypes();
  }, []);

  const loadServiceTypes = async () => {
    try {
      const response = await axios.get(`${API_URL}/service-types`);
      setServiceTypes(response.data);
      setError('');
    } catch (error) {
      setError(error.response?.data || 'Có lỗi xảy ra khi tải danh sách dịch vụ');
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'unitPrice' ? parseFloat(value) || '' : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      if (selectedServiceType) {
        await axios.put(
          `${API_URL}/service-types/${selectedServiceType.serviceTypeId}`,
          formData
        );
        setSuccess('Cập nhật dịch vụ thành công');
      } else {
        await axios.post(`${API_URL}/service-types`, formData);
        setSuccess('Thêm dịch vụ mới thành công');
      }

      loadServiceTypes();
      resetForm();
    } catch (error) {
      setError(error.response?.data || 'Có lỗi xảy ra khi lưu dịch vụ');
    }
  };

  const handleEdit = (serviceType) => {
    setSelectedServiceType(serviceType);
    setFormData({
      serviceName: serviceType.serviceName,
      serviceType: serviceType.serviceType,
      unitPrice: serviceType.unitPrice,
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa dịch vụ này?')) return;

    try {
      await axios.delete(`${API_URL}/service-types/${id}`);
      setSuccess('Xóa dịch vụ thành công');
      loadServiceTypes();
    } catch (error) {
      setError(error.response?.data || 'Có lỗi xảy ra khi xóa dịch vụ');
    }
  };

  const resetForm = () => {
    setSelectedServiceType(null);
    setFormData({
      serviceName: '',
      serviceType: 'electricity',
      unitPrice: '',
    });
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(amount);
  };

  const getServiceTypeText = (type) => {
    const serviceTypes = {
      electricity: 'Điện',
      water: 'Nước',
      motorbike: 'Gửi xe máy',
      car: 'Gửi ô tô',
      management: 'Quản lý',
      maintenance: 'Bảo trì',
    };
    return serviceTypes[type] || type;
  };

  return (
    <div className="service-type-management">
      <h2>Quản lý dịch vụ</h2>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      <form onSubmit={handleSubmit} className="service-type-form">
        <div className="form-group">
          <label>Tên dịch vụ:</label>
          <input
            type="text"
            name="serviceName"
            value={formData.serviceName}
            onChange={handleInputChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Loại dịch vụ:</label>
          <select
            name="serviceType"
            value={formData.serviceType}
            onChange={handleInputChange}
            required
          >
            <option value="electricity">Điện</option>
            <option value="water">Nước</option>
            <option value="motorbike">Gửi xe máy</option>
            <option value="car">Gửi ô tô</option>
            <option value="management">Quản lý</option>
            <option value="maintenance">Bảo trì</option>
          </select>
        </div>

        <div className="form-group">
          <label>Đơn giá:</label>
          <input
            type="number"
            name="unitPrice"
            value={formData.unitPrice}
            onChange={handleInputChange}
            min="0"
            step="1000"
            required
          />
        </div>

        <div className="button-group">
          <button type="submit">
            {selectedServiceType ? 'Cập nhật' : 'Thêm mới'}
          </button>
          {selectedServiceType && (
            <button type="button" onClick={resetForm}>
              Hủy
            </button>
          )}
        </div>
      </form>

      <div className="service-type-list">
        <h3>Danh sách dịch vụ</h3>
        <div className="service-type-grid">
          {serviceTypes.map((service) => (
            <div key={service.serviceTypeId} className="service-type-card">
              <h4>{service.serviceName}</h4>
              <p>Loại: {getServiceTypeText(service.serviceType)}</p>
              <p>Đơn giá: {formatCurrency(service.unitPrice)}</p>
              <div className="card-actions">
                <button
                  className="edit-button"
                  onClick={() => handleEdit(service)}
                >
                  Sửa
                </button>
                <button
                  className="delete-button"
                  onClick={() => handleDelete(service.serviceTypeId)}
                >
                  Xóa
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ServiceTypeManagement; 