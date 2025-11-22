import { ITenant } from 'app/shared/model/tenant.model';

export interface IEncryption {
  id?: number;
  key?: string;
  pin?: string;
  tenant?: ITenant | null;
}

export const defaultValue: Readonly<IEncryption> = {};
