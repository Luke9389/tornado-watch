import React from 'react';
import { Alert, AlertSeverity } from '../types/Alert';
import './AlertCard.css';

interface AlertCardProps {
  alert: Alert;
}

const AlertCard: React.FC<AlertCardProps> = ({ alert }) => {
  const getSeverityClass = (severity: AlertSeverity): string => {
    switch (severity) {
      case AlertSeverity.HIGH:
        return 'severity-high';
      case AlertSeverity.MODERATE:
        return 'severity-moderate';
      case AlertSeverity.LOW:
        return 'severity-low';
      default:
        return '';
    }
  };

  const formatDateTime = (dateString: string): string => {
    return new Date(dateString).toLocaleString();
  };

  const isExpired = (): boolean => {
    if (!alert.expiresAt) return false;
    return new Date(alert.expiresAt) < new Date();
  };

  return (
    <div className={`alert-card ${getSeverityClass(alert.severity)} ${isExpired() ? 'expired' : ''}`}>
      <div className="alert-header">
        <h3 className="alert-title">{alert.title}</h3>
        <div className="alert-badges">
          <span className={`severity-badge ${getSeverityClass(alert.severity)}`}>
            {alert.severity}
          </span>
          <span className="region-badge">{alert.region}</span>
        </div>
      </div>
      
      <p className="alert-description">{alert.description}</p>
      
      <div className="alert-times">
        <div className="time-info">
          <strong>Issued:</strong> {formatDateTime(alert.issuedAt)}
        </div>
        {alert.expiresAt && (
          <div className="time-info">
            <strong>Expires:</strong> {formatDateTime(alert.expiresAt)}
            {isExpired() && <span className="expired-label"> (EXPIRED)</span>}
          </div>
        )}
      </div>
    </div>
  );
};

export default AlertCard;