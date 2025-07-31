export enum AlertSeverity {
  LOW = 'LOW',
  MODERATE = 'MODERATE',
  HIGH = 'HIGH'
}

export interface Alert {
  id: number;
  title: string;
  description: string;
  severity: AlertSeverity;
  region: string;
  issuedAt: string;
  expiresAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface AlertFilters {
  region?: string;
  severity?: AlertSeverity;
  startDate?: string;
  endDate?: string;
}