import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactAccountDetailComponent } from './contact-account-detail.component';

describe('Component Tests', () => {
  describe('ContactAccount Management Detail Component', () => {
    let comp: ContactAccountDetailComponent;
    let fixture: ComponentFixture<ContactAccountDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactAccountDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactAccount: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactAccount).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
