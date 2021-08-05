import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactReminderMySuffixComponent } from '../list/contact-reminder-my-suffix.component';
import { ContactReminderMySuffixDetailComponent } from '../detail/contact-reminder-my-suffix-detail.component';
import { ContactReminderMySuffixUpdateComponent } from '../update/contact-reminder-my-suffix-update.component';
import { ContactReminderMySuffixRoutingResolveService } from './contact-reminder-my-suffix-routing-resolve.service';

const contactReminderRoute: Routes = [
  {
    path: '',
    component: ContactReminderMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactReminderMySuffixDetailComponent,
    resolve: {
      contactReminder: ContactReminderMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactReminderMySuffixUpdateComponent,
    resolve: {
      contactReminder: ContactReminderMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactReminderMySuffixUpdateComponent,
    resolve: {
      contactReminder: ContactReminderMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactReminderRoute)],
  exports: [RouterModule],
})
export class ContactReminderMySuffixRoutingModule {}
