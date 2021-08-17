import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplicationRoleDetailComponent } from './application-role-detail.component';

describe('Component Tests', () => {
  describe('ApplicationRole Management Detail Component', () => {
    let comp: ApplicationRoleDetailComponent;
    let fixture: ComponentFixture<ApplicationRoleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApplicationRoleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ applicationRole: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApplicationRoleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApplicationRoleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load applicationRole on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.applicationRole).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
