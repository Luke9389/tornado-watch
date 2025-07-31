import React, { useState, useEffect } from 'react';
import { Alert, AlertFilters } from '../types/Alert';
import { AlertService } from '../services/alertService';
import AlertCard from './AlertCard';
import AlertFiltersComponent from './AlertFilters';
import './AlertList.css';

const AlertList: React.FC = () => {
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [filters, setFilters] = useState<AlertFilters>({});

  const fetchAlerts = async (currentFilters: AlertFilters) => {
    try {
      setLoading(true);
      setError(null);
      const fetchedAlerts = await AlertService.getAlerts(currentFilters);
      setAlerts(fetchedAlerts);
    } catch (err) {
      setError('Failed to fetch alerts. Please try again later.');
      console.error('Error fetching alerts:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAlerts(filters);
  }, [filters]);

  const handleFiltersChange = (newFilters: AlertFilters) => {
    setFilters(newFilters);
  };

  const refreshAlerts = () => {
    fetchAlerts(filters);
  };

  if (loading) {
    return (
      <div className="alert-list-container">
        <div className="loading">Loading alerts...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert-list-container">
        <div className="error">
          <p>{error}</p>
          <button onClick={refreshAlerts} className="retry-btn">
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="alert-list-container">
      <header className="alert-list-header">
        <h1>Tornado Watch Alerts</h1>
        <button onClick={refreshAlerts} className="refresh-btn">
          Refresh
        </button>
      </header>

      <AlertFiltersComponent 
        onFiltersChange={handleFiltersChange}
        initialFilters={filters}
      />

      <div className="alert-results">
        <div className="results-header">
          <h2>
            {alerts.length} Alert{alerts.length !== 1 ? 's' : ''} Found
          </h2>
        </div>

        {alerts.length === 0 ? (
          <div className="no-alerts">
            <p>No alerts match your current filters.</p>
          </div>
        ) : (
          <div className="alerts-grid">
            {alerts.map((alert) => (
              <AlertCard key={alert.id} alert={alert} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default AlertList;