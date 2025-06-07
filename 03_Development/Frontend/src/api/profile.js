import axios from './axios'

export const getUserProfile = async (userId) => {
  if (!userId) {
    throw new Error('userId is required')
  }
  const response = await axios.get(`/users/profile/${userId}`)
  return response.data
}

export const updateUserProfile = async (userId, data) => {
  if (!userId) {
    throw new Error('userId is required')
  }
  const response = await axios.patch(`/users/profile/${userId}`, data)
  return response.data
} 