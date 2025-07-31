import { Alert, AlertFilters } from '../types/Alert';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export class AlertService {
  static async getAlerts(filters?: AlertFilters): Promise<Alert[]> {
    const params = new URLSearchParams();
    
    if (filters?.region) {
      params.append('region', filters.region);
    }
    if (filters?.severity) {
      params.append('severity', filters.severity);
    }
    if (filters?.startDate) {
      params.append('startDate', filters.startDate);
    }
    if (filters?.endDate) {
      params.append('endDate', filters.endDate);
    }

    const url = `${API_BASE_URL}/api/alerts${params.toString() ? `?${params.toString()}` : ''}`;
    
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Failed to fetch alerts:', error);
      throw error;
    }
  }

  static async getAlert(id: number): Promise<Alert> {
    const url = `${API_BASE_URL}/api/alerts/${id}`;
    
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error(`Failed to fetch alert ${id}:`, error);
      throw error;
    }
  }
}