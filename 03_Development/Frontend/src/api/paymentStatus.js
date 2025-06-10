import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

// API cho cư dân
export const getResidentPayments = async (ownershipId) => {
  try {
    const response = await axios.get(`${API_URL}/resident/payment-status/${ownershipId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching resident payments:', error);
    throw error;
  }
};

export const getResidentPaymentsByStatus = async (ownershipId, status) => {
  try {
    const response = await axios.get(`${API_URL}/resident/payment-status/${ownershipId}/by-status`, {
      params: { status }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching resident payments by status:', error);
    throw error;
  }
};

// API cho kế toán
export const getAccountantPayments = async (filters = {}) => {
  try {
    const response = await axios.get(`${API_URL}/accountant/payment-status`, { params: filters });
    return response.data;
  } catch (error) {
    console.error('Error fetching accountant payments:', error);
    throw error;
  }
};

export const getAccountantPaymentsByStatus = async (status, filters = {}) => {
  try {
    const response = await axios.get(`${API_URL}/accountant/payment-status/by-status`, {
      params: { status, ...filters }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching accountant payments by status:', error);
    throw error;
  }
};

export const getPaymentPeriods = async () => {
  try {
    const response = await axios.get(`${API_URL}/accountant/payment-periods`);
    return response.data;
  } catch (error) {
    console.error('Error fetching payment periods:', error);
    throw error;
  }
};

export const getApartments = async () => {
  try {
    const response = await axios.get(`${API_URL}/accountant/apartments`);
    return response.data;
  } catch (error) {
    console.error('Error fetching apartments:', error);
    throw error;
  }
};

export const updatePaymentStatus = async (data) => {
  try {
    const response = await axios.put(`${API_URL}/accountant/payment-status/update`, data);
    return response.data;
  } catch (error) {
    console.error('Error updating payment status:', error);
    throw error;
  }
};

export const updatePaymentDetail = async (data) => {
  try {
    const response = await axios.patch(`${API_URL}/accountant/payment-status/update-detail`, data);
    return response.data;
  } catch (error) {
    console.error('Error updating payment detail:', error);
    throw error;
  }
}; 