import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { IEffort } from 'app/entities/effort/effort.model';

export interface IWorkingHour {
  id?: number;
  remoteId?: number | null;
  userName?: string | null;
  date?: dayjs.Dayjs | null;
  timeStart?: dayjs.Dayjs | null;
  timeEnd?: dayjs.Dayjs | null;
  created?: dayjs.Dayjs | null;
  applicationUser?: IApplicationUser | null;
  effort?: IEffort | null;
}

export class WorkingHour implements IWorkingHour {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public userName?: string | null,
    public date?: dayjs.Dayjs | null,
    public timeStart?: dayjs.Dayjs | null,
    public timeEnd?: dayjs.Dayjs | null,
    public created?: dayjs.Dayjs | null,
    public applicationUser?: IApplicationUser | null,
    public effort?: IEffort | null
  ) {}
}

export function getWorkingHourIdentifier(workingHour: IWorkingHour): number | undefined {
  return workingHour.id;
}
