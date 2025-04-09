export type UserRole = 'FOUNDER' | 'INVESTOR' | 'ADMIN';

export interface User {
  id: number;
  email: string;
  role: UserRole;
  name: string;
  createdAt: string;
  status: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  role: UserRole;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface Founder extends User {
  companyName: string;
  totalRaised: number;
  projects: Project[];
}

export interface Investor extends User {
  totalInvested: number;
  investments: Investment[];
}

export interface Project {
  id: string;
  name: string;
  description: string;
  targetAmount: number;
  raisedAmount: number;
  founderId: string;
  status: 'active' | 'closed';
  createdAt: string;
}

export interface Investment {
  id: string;
  projectId: string;
  investorId: string;
  amount: number;
  createdAt: string;
  project: Project;
} 