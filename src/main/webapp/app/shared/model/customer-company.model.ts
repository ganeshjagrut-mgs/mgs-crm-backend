export interface ICustomerCompany {
  id?: number;
  name?: string;
  code?: string | null;
  description?: string | null;
  website?: string | null;
  registrationNumber?: string | null;
}

export const defaultValue: Readonly<ICustomerCompany> = {};
