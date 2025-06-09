import axios from 'axios';
import { API_URL } from '../constants/api';

const PAYMENT_STATUS_API = `${API_URL}/accountant/payment-status`;
const ACCOUNTANT_API = `${API_URL}/accountant`;

export const getAllPayments = async (filters = {}) => {
  try {
    const response = await axios.get(PAYMENT_STATUS_API, {
      params: filters
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Có lỗi xảy ra khi tải danh sách thanh toán');
  }
};

export const getPaymentsByStatus = async (status, filters = {}) => {
  try {
    const response = await axios.get(`${PAYMENT_STATUS_API}/by-status`, {
      params: { status, ...filters }
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Có lỗi xảy ra khi tải danh sách thanh toán');
  }
};

export const updatePaymentStatus = async (request) => {
  try {
    const response = await axios.put(`${PAYMENT_STATUS_API}/update`, request);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Có lỗi xảy ra khi cập nhật trạng thái thanh toán');
  }
};

// API để lấy danh sách kỳ thu phí
export const getPaymentPeriods = async () => {
  try {
    const response = await axios.get(`${ACCOUNTANT_API}/payment-periods`);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Có lỗi xảy ra khi tải danh sách kỳ thu phí');
  }
};

// API để lấy danh sách căn hộ
export const getApartments = async () => {
  try {
    const response = await axios.get(`${ACCOUNTANT_API}/apartments`);
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Có lỗi xảy ra khi tải danh sách căn hộ');
  }
}; 