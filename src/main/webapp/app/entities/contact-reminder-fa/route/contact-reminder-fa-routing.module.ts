import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactReminderFaComponent } from '../list/contact-reminder-fa.component';
import { ContactReminderFaDetailComponent } from '../detail/contact-reminder-fa-detail.component';
import { ContactReminderFaUpdateComponent } from '../update/contact-reminder-fa-update.component';
import { ContactReminderFaRoutingResolveService } from './contact-reminder-fa-routing-resolve.service';

const contactReminderRoute: Routes = [
  {
    path: '',
    component: ContactReminderFaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactReminderFaDetailComponent,
    resolve: {
      contactReminder: ContactReminderFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactReminderFaUpdateComponent,
    resolve: {
      contactReminder: ContactReminderFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactReminderFaUpdateComponent,
    resolve: {
      contactReminder: ContactReminderFaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactReminderRoute)],
  exports: [RouterModule],
})
export class ContactReminderFaRoutingModule {}
