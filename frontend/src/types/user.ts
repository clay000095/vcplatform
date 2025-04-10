export type UserRole = 'ADMIN' | 'FOUNDER' | 'INVESTOR';

export interface User {
  id: number;
  email: string;
  password?: string;  // 可選，因為登入後可能不需要
  role: UserRole;
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
  token?: string;  // 可選，因為後端可能不返回
  user: User;
}

export interface Founder extends User {
  companyName?: string;
  totalRaised?: number;
  projects?: Project[];
}

export interface Investor extends User {
  totalInvested?: number;
  investments?: Investment[];
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