import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactGroupDetailComponent } from './contact-group-detail.component';

describe('Component Tests', () => {
  describe('ContactGroup Management Detail Component', () => {
    let comp: ContactGroupDetailComponent;
    let fixture: ComponentFixture<ContactGroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactGroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactGroup).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
