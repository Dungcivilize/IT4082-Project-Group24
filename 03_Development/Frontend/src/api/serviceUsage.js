import axios from './axios'

const API_URL = "http://localhost:8080/api";

export const createPaymentDetail = async (data) => {
  try {
    const response = await axios.post(`${API_URL}/accountant/service-usage`, data)
    return response.data
  } catch (error) {
    console.error("Error creating payment detail:", error);
    throw error.response?.data?.message || 'Có lỗi xảy ra'
  }
}

export const getAllPaymentDetails = async () => {
  try {
    const response = await axios.get(`${API_URL}/accountant/service-usage`)
    return response.data
  } catch (error) {
    console.error("Error fetching payment details:", error)
    throw error.response?.data?.message || 'Có lỗi xảy ra'
  }
}

export const getAllPayments = async () => {
  try {
    const response = await axios.get(`${API_URL}/accountant/payment-status`)
    return response.data
  } catch (error) {
    throw error.response?.data?.message || 'Có lỗi xảy ra'
  }
}

export const getPaymentsByStatus = async (status) => {
  try {
    const response = await axios.get(`${API_URL}/accountant/payment-status/by-status?status=${status}`)
    return response.data
  } catch (error) {
    throw error.response?.data?.message || 'Có lỗi xảy ra'
  }
}

export const updatePaymentStatus = async (data) => {
  try {
    const response = await axios.put(`${API_URL}/accountant/payment-status/update`, data)
    return response.data
  } catch (error) {
    throw error.response?.data?.message || 'Có lỗi xảy ra'
  }
} 