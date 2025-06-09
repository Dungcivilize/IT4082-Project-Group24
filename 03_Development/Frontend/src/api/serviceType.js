import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const getAllServiceTypes = async () => {
  try {
    const response = await axios.get(`${API_URL}/service-types`);
    return response.data;
  } catch (error) {
    console.error("Error fetching service types:", error);
    throw error;
  }
}; 