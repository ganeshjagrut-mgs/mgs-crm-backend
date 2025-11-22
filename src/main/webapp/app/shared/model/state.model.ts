import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';

export interface IState {
  id?: number;
  name?: string;
  code?: string | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  active?: boolean | null;
  country?: ICountry | null;
}

export const defaultValue: Readonly<IState> = {
  active: false,
};
