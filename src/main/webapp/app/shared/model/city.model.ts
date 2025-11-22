import dayjs from 'dayjs';
import { IState } from 'app/shared/model/state.model';

export interface ICity {
  id?: number;
  name?: string;
  postalCode?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  active?: boolean | null;
  state?: IState | null;
}

export const defaultValue: Readonly<ICity> = {
  active: false,
};
