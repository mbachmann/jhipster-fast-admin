import * as dayjs from 'dayjs';
import { ICustomFieldValue } from 'app/entities/custom-field-value/custom-field-value.model';
import { IContact } from 'app/entities/contact/contact.model';
import { ProjectStatus } from 'app/entities/enumerations/project-status.model';

export interface IProject {
  id?: number;
  remoteId?: number | null;
  number?: string | null;
  contactName?: string | null;
  name?: string;
  description?: string | null;
  startDate?: dayjs.Dayjs;
  hoursEstimated?: number | null;
  hourlyRate?: number | null;
  status?: ProjectStatus;
  customFields?: ICustomFieldValue[] | null;
  contact?: IContact | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public number?: string | null,
    public contactName?: string | null,
    public name?: string,
    public description?: string | null,
    public startDate?: dayjs.Dayjs,
    public hoursEstimated?: number | null,
    public hourlyRate?: number | null,
    public status?: ProjectStatus,
    public customFields?: ICustomFieldValue[] | null,
    public contact?: IContact | null
  ) {}
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
