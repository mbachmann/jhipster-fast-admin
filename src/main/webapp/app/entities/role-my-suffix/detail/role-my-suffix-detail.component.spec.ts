import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoleMySuffixDetailComponent } from './role-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('RoleMySuffix Management Detail Component', () => {
    let comp: RoleMySuffixDetailComponent;
    let fixture: ComponentFixture<RoleMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RoleMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ role: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RoleMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoleMySuffixDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load role on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.role).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
