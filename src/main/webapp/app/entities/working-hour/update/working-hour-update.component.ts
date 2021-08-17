import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWorkingHour, WorkingHour } from '../working-hour.model';
import { WorkingHourService } from '../service/working-hour.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IEffort } from 'app/entities/effort/effort.model';
import { EffortService } from 'app/entities/effort/service/effort.service';

@Component({
  selector: 'fa-working-hour-update',
  templateUrl: './working-hour-update.component.html',
})
export class WorkingHourUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  effortsSharedCollection: IEffort[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    userName: [],
    date: [],
    timeStart: [],
    timeEnd: [],
    created: [],
    applicationUser: [],
    effort: [],
  });

  constructor(
    protected workingHourService: WorkingHourService,
    protected applicationUserService: ApplicationUserService,
    protected effortService: EffortService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingHour }) => {
      if (workingHour.id === undefined) {
        const today = dayjs().startOf('day');
        workingHour.timeStart = today;
        workingHour.timeEnd = today;
        workingHour.created = today;
      }

      this.updateForm(workingHour);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workingHour = this.createFromForm();
    if (workingHour.id !== undefined) {
      this.subscribeToSaveResponse(this.workingHourService.update(workingHour));
    } else {
      this.subscribeToSaveResponse(this.workingHourService.create(workingHour));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackEffortById(index: number, item: IEffort): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkingHour>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workingHour: IWorkingHour): void {
    this.editForm.patchValue({
      id: workingHour.id,
      remoteId: workingHour.remoteId,
      userName: workingHour.userName,
      date: workingHour.date,
      timeStart: workingHour.timeStart ? workingHour.timeStart.format(DATE_TIME_FORMAT) : null,
      timeEnd: workingHour.timeEnd ? workingHour.timeEnd.format(DATE_TIME_FORMAT) : null,
      created: workingHour.created ? workingHour.created.format(DATE_TIME_FORMAT) : null,
      applicationUser: workingHour.applicationUser,
      effort: workingHour.effort,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      workingHour.applicationUser
    );
    this.effortsSharedCollection = this.effortService.addEffortToCollectionIfMissing(this.effortsSharedCollection, workingHour.effort);
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('applicationUser')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.effortService
      .query()
      .pipe(map((res: HttpResponse<IEffort[]>) => res.body ?? []))
      .pipe(map((efforts: IEffort[]) => this.effortService.addEffortToCollectionIfMissing(efforts, this.editForm.get('effort')!.value)))
      .subscribe((efforts: IEffort[]) => (this.effortsSharedCollection = efforts));
  }

  protected createFromForm(): IWorkingHour {
    return {
      ...new WorkingHour(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      userName: this.editForm.get(['userName'])!.value,
      date: this.editForm.get(['date'])!.value,
      timeStart: this.editForm.get(['timeStart'])!.value ? dayjs(this.editForm.get(['timeStart'])!.value, DATE_TIME_FORMAT) : undefined,
      timeEnd: this.editForm.get(['timeEnd'])!.value ? dayjs(this.editForm.get(['timeEnd'])!.value, DATE_TIME_FORMAT) : undefined,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
      effort: this.editForm.get(['effort'])!.value,
    };
  }
}
