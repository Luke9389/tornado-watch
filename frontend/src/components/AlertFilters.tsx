import React, { useState } from 'react';
import { AlertSeverity, AlertFilters } from '../types/Alert';
import './AlertFilters.css';

interface AlertFiltersProps {
  onFiltersChange: (filters: AlertFilters) => void;
  initialFilters?: AlertFilters;
}

const AlertFiltersComponent: React.FC<AlertFiltersProps> = ({ 
  onFiltersChange, 
  initialFilters = {} 
}) => {
  const [filters, setFilters] = useState<AlertFilters>(initialFilters);

  const handleInputChange = (field: keyof AlertFilters, value: string) => {
    const newFilters = {
      ...filters,
      [field]: value || undefined
    };
    setFilters(newFilters);
    onFiltersChange(newFilters);
  };

  const clearFilters = () => {
    const emptyFilters = {};
    setFilters(emptyFilters);
    onFiltersChange(emptyFilters);
  };

  const hasActiveFilters = Object.values(filters).some(value => value);

  return (
    <div className="alert-filters">
      <h3>Filter Alerts</h3>
      
      <div className="filters-grid">
        <div className="filter-group">
          <label htmlFor="region">Region:</label>
          <input
            id="region"
            type="text"
            placeholder="e.g., Oklahoma, Kansas"
            value={filters.region || ''}
            onChange={(e) => handleInputChange('region', e.target.value)}
          />
        </div>

        <div className="filter-group">
          <label htmlFor="severity">Severity:</label>
          <select
            id="severity"
            value={filters.severity || ''}
            onChange={(e) => handleInputChange('severity', e.target.value)}
          >
            <option value="">All Severities</option>
            <option value={AlertSeverity.LOW}>Low</option>
            <option value={AlertSeverity.MODERATE}>Moderate</option>
            <option value={AlertSeverity.HIGH}>High</option>
          </select>
        </div>

        <div className="filter-group">
          <label htmlFor="startDate">Start Date:</label>
          <input
            id="startDate"
            type="datetime-local"
            value={filters.startDate || ''}
            onChange={(e) => handleInputChange('startDate', e.target.value)}
          />
        </div>

        <div className="filter-group">
          <label htmlFor="endDate">End Date:</label>
          <input
            id="endDate"
            type="datetime-local"
            value={filters.endDate || ''}
            onChange={(e) => handleInputChange('endDate', e.target.value)}
          />
        </div>
      </div>

      {hasActiveFilters && (
        <button className="clear-filters-btn" onClick={clearFilters}>
          Clear All Filters
        </button>
      )}
    </div>
  );
};

export default AlertFiltersComponent;