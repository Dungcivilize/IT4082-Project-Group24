import axios from 'axios'
import { API_URL } from '../constants/api'

export const getResidentsByUser = async (userId) => {
  try {
    const response = await axios.get(`${API_URL}/residents/user/${userId}`)
    return response.data
  } catch (error) {
    throw error.response?.data || 'Có lỗi xảy ra khi lấy danh sách thành viên'
  }
}

export const addResident = async (userId, residentData) => {
  try {
    // Xử lý dữ liệu trước khi gửi
    const cleanData = Object.keys(residentData).reduce((acc, key) => {
      if (residentData[key] !== null && residentData[key] !== undefined && residentData[key] !== '') {
        acc[key] = residentData[key]
      }
      return acc
    }, {})

    const response = await axios.post(`${API_URL}/residents/user/addresident/${userId}`, cleanData)
    return response.data
  } catch (error) {
    throw error.response?.data || 'Có lỗi xảy ra khi thêm thành viên'
  }
}

export const updateResident = async (userId, residentId, residentData) => {
  try {
    // Xử lý dữ liệu trước khi gửi
    const cleanData = Object.keys(residentData).reduce((acc, key) => {
      if (residentData[key] !== null && residentData[key] !== undefined && residentData[key] !== '') {
        acc[key] = residentData[key]
      }
      return acc
    }, {})

    const response = await axios.patch(`${API_URL}/residents/user/${userId}/resident/${residentId}`, cleanData)
    return response.data
  } catch (error) {
    throw error.response?.data || 'Có lỗi xảy ra khi cập nhật thông tin thành viên'
  }
}

export const deleteResident = async (userId, residentId) => {
  try {
    await axios.delete(`${API_URL}/residents/user/${userId}/resident/${residentId}`)
  } catch (error) {
    throw error.response?.data || 'Có lỗi xảy ra khi xóa thành viên'
  }
} 